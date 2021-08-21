package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.config.MobileConfig;
import com.churchofphilippi.webserver.exception.exceptionModel.FCMCredentialNotFoundException;
import com.churchofphilippi.webserver.model.*;
import com.churchofphilippi.webserver.repository.DeptMemberRepository;
import com.churchofphilippi.webserver.repository.FCMTokenRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class FCMTokenService implements BaseService<FCMToken> {

    @Autowired
    private final FCMTokenRepository fcmTokenRepository;
    @Autowired
    private final MobileConfig mobileConfig;
    @Autowired
    private final DeptMemberRepository deptMemberRepository;
    @Autowired
    private final ResourceLoader resourceLoader;


    public FCMTokenService(FCMTokenRepository fcmTokenRepository, MobileConfig mobileConfig, DeptMemberRepository deptMemberRepository, ResourceLoader resourceLoader) {
        this.fcmTokenRepository = fcmTokenRepository;
        this.mobileConfig = mobileConfig;
        this.deptMemberRepository = deptMemberRepository;
        this.resourceLoader = resourceLoader;
        try {

            Resource resource = this.resourceLoader.getResource("classpath:" + mobileConfig.getFcmAdminSDK());
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions.Builder builder = FirebaseOptions.builder();
            FirebaseOptions options = builder.
                    setCredentials(GoogleCredentials.fromStream(serviceAccount)).
                    build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
           throw new FCMCredentialNotFoundException("FCM token not found");
        }
    }

    @Override
    public List<FCMToken> getAll() {
        return fcmTokenRepository.findAll();
    }

    @Override
    public FCMToken save(FCMToken entity) {
        return fcmTokenRepository.save(entity);
    }

    @Override
    public void delete(FCMToken entity) {
        fcmTokenRepository.delete(entity);
    }

    public FCMToken findOne(FCMToken token) {
        return fcmTokenRepository.findByMemberAndDeviceId(token.getMember(), token.getDeviceId());
    }

    public FCMToken updateToken(FCMToken token) {
        FCMToken fcmToken = findOne(token);
        FCMToken ret;

        if(fcmToken == null) {
            ret = fcmTokenRepository.save(token);
            resubscribeToTopic(token);
        }
        else if(fcmToken.getToken().compareTo(token.getToken()) != 0) {
            fcmToken.setToken(token.getToken());
            ret = fcmTokenRepository.save(fcmToken);
            resubscribeToTopic(token);
        } else {
            ret = token;
        }

        return ret;
    }

    public FCMToken getByMember(AllMembers member) {
        return fcmTokenRepository.findByMember(member);
    }

    public void subscribeToTopic(AllMembers member, String topicName) {
        try{
            FCMToken token = getByMember(member);
            if(token != null) {
                List<String> registrationTokens = Collections.singletonList(token.getToken());

                TopicManagementResponse response = FirebaseMessaging.
                        getInstance().
                        subscribeToTopic(registrationTokens, topicName);

                System.out.println(response.getSuccessCount() + " token was subscribed successfully");
            }

        }catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribeFromTopic(AllMembers member, String topicName) {
        try{
            FCMToken token = getByMember(member);
            if(token != null) {
                List<String> registrationTokens = Collections.singletonList(token.getToken());

                TopicManagementResponse response = FirebaseMessaging.
                        getInstance().
                        unsubscribeFromTopic(registrationTokens, topicName);

                System.out.println(response.getSuccessCount() + " token was subscribed successfully");
            }

        }catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String title, String body, String topic)  {
        try{
            Notification notification = Notification.builder().
                    setTitle(title).
                    setBody(body).
                    setImage(mobileConfig.getNotificationIcon()).
                    build();
            Message message = Message.builder().
                    setNotification(notification).
                    setTopic(topic).
                    build();
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        }catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public String setTopic(Dept dept) {
        String topic = mobileConfig.getNotificationTopic();
        if(dept != null && dept.getDeptId() != null) {
            topic = getTopicFromDept(dept);
        }
        return topic;
    }

    public void resubscribeToTopic(FCMToken token) {
        try {
            List<DeptMember> deptMembers = deptMemberRepository.findDeptsByMemberId(token.getMember().getMemberId());
            for(DeptMember dm : deptMembers) {
                Dept dept = dm.getDepartment();
                String topic = getTopicFromDept(dept);
                List<String> registrationTokens = Collections.singletonList(token.getToken());
                FirebaseMessaging.getInstance().subscribeToTopic(registrationTokens, topic);
                Dept parentDept = dept.getParentDept();
                if(parentDept != null && parentDept.getDeptId() != null) {
                    topic = getTopicFromDept(parentDept);
                    FirebaseMessaging.getInstance().subscribeToTopic(registrationTokens, topic);
                }
            }
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public String getTopicFromDept(Dept dept) {
        return "department" + dept.getDeptId();
    }
}
