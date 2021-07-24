package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.registration.EmailValidator;
import com.churchofphilippi.webserver.registration.RegistrationRequest;
import com.churchofphilippi.webserver.registration.emailPassword.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final MemberService memberService;
    private final EmailSender emailSender;

    public Member register(RegistrationRequest request){
        boolean isValidEMail = emailValidator.test(request.getEmail());
        if(!isValidEMail) {
            throw new IllegalStateException("Email not valid");
        }

        String password = generatePassword();

        Member ret = memberService.registerMember(
                        new Member(
                                request.getFname(),
                                request.getLname(),
                                request.getGender(),
                                LocalDate.now(),
                                request.getStreetName(),
                                request.getStreetAddress(),
                                request.getAptNumber(),
                                request.getCity(),
                                request.getState(),
                                request.getZipCode(),
                                request.getEmail(),
                                request.getPhoneNumber(),
                                password
                        )
        );

        emailSender.send(request.getEmail(), buildHtml(request.getFname(), password));

        return ret;

    }

    private static String generatePassword() {
        int length = 8;
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return String.valueOf(password);
    }

    private String buildHtml(String firstname, String password) {
        String res  =

                   "<!DOCTYPE html>\n" +
                   "<html>\n" +
                   "<head>\n" +
                   "\n" +
                   "    <meta charset=\"utf-8\">\n" +
                   "    <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
                   "    <title>Password Reset</title>\n" +
                   "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                   "    <style type=\"text/css\">\n" +
                   "        /**\n" +
                   "         * Google webfonts. Recommended to include the .woff version for cross-client compatibility.\n" +
                   "         */\n" +
                   "        @media screen {\n" +
                   "            @font-face {\n" +
                   "                font-family: 'Source Sans Pro';\n" +
                   "                font-style: normal;\n" +
                   "                font-weight: 400;\n" +
                   "                src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');\n" +
                   "            }\n" +
                   "\n" +
                   "            @font-face {\n" +
                   "                font-family: 'Source Sans Pro';\n" +
                   "                font-style: normal;\n" +
                   "                font-weight: 700;\n" +
                   "                src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');\n" +
                   "            }\n" +
                   "        }\n" +
                   "\n" +
                   "        /**\n" +
                   "         * Avoid browser level font resizing.\n" +
                   "         * 1. Windows Mobile\n" +
                   "         * 2. iOS / OSX\n" +
                   "         */\n" +
                   "        body,\n" +
                   "        table,\n" +
                   "        td,\n" +
                   "        a {\n" +
                   "            -ms-text-size-adjust: 100%; /* 1 */\n" +
                   "            -webkit-text-size-adjust: 100%; /* 2 */\n" +
                   "        }\n" +
                   "\n" +
                   "        /**\n" +
                   "         * Remove extra space added to tables and cells in Outlook.\n" +
                   "         */\n" +
                   "        table,\n" +
                   "        td {\n" +
                   "            mso-table-rspace: 0pt;\n" +
                   "            mso-table-lspace: 0pt;\n" +
                   "        }\n" +
                   "\n" +
                   "        /**\n" +
                   "         * Better fluid images in Internet Explorer.\n" +
                   "         */\n" +
                   "        img {\n" +
                   "            -ms-interpolation-mode: bicubic;\n" +
                   "        }\n" +
                   "\n" +
                   "        /**\n" +
                   "         * Remove blue links for iOS devices.\n" +
                   "         */\n" +
                   "        a[x-apple-data-detectors] {\n" +
                   "            font-family: inherit !important;\n" +
                   "            font-size: inherit !important;\n" +
                   "            font-weight: inherit !important;\n" +
                   "            line-height: inherit !important;\n" +
                   "            color: inherit !important;\n" +
                   "            text-decoration: none !important;\n" +
                   "        }\n" +
                   "\n" +
                   "        /**\n" +
                   "         * Fix centering issues in Android 4.4.\n" +
                   "         */\n" +
                   "        div[style*=\"margin: 16px 0;\"] {\n" +
                   "            margin: 0 !important;\n" +
                   "        }\n" +
                   "\n" +
                   "        body {\n" +
                   "            width: 100% !important;\n" +
                   "            height: 100% !important;\n" +
                   "            padding: 0 !important;\n" +
                   "            margin: 0 !important;\n" +
                   "        }\n" +
                   "\n" +
                   "        /**\n" +
                   "         * Collapse table borders to avoid space between cells.\n" +
                   "         */\n" +
                   "        table {\n" +
                   "            border-collapse: collapse !important;\n" +
                   "        }\n" +
                   "\n" +
                   "        a {\n" +
                   "            color: #1a82e2;\n" +
                   "        }\n" +
                   "\n" +
                   "        img {\n" +
                   "            height: auto;\n" +
                   "            line-height: 100%;\n" +
                   "            text-decoration: none;\n" +
                   "            border: 0;\n" +
                   "            outline: none;\n" +
                   "        }\n" +
                   "    </style>\n" +
                   "\n" +
                   "</head>\n" +
                   "<body style=\"background-color: #e9ecef;\">\n" +
                   "\n" +
                   "<!-- start preheader -->\n" +
                   "<div class=\"preheader\" style=\"display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;\">\n" +
                   "    Welcome to church of Philippi. Please use this temporary Password to log in to our app.\n" +
                   "</div>\n" +
                   "<!-- end preheader -->\n" +
                   "\n" +
                   "<!-- start body -->\n" +
                   "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                   "\n" +
                   "    <!-- start logo -->\n" +
                   "    <tr>\n" +
                   "        <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                   "            <![endif]-->\n" +
                   "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center\" valign=\"top\" style=\"padding: 36px 24px;\">\n" +
                   "\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "            </table>\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            </td>\n" +
                   "            </tr>\n" +
                   "            </table>\n" +
                   "            <![endif]-->\n" +
                   "        </td>\n" +
                   "    </tr>\n" +
                   "    <!-- end logo -->\n" +
                   "\n" +
                   "    <!-- start hero -->\n" +
                   "    <tr>\n" +
                   "        <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                   "            <![endif]-->\n" +
                   "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                   "                <tr>\n" +
                   "                    <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;\">\n" +
                   "                        <h1 style=\"margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;\">Welcome to church of philippi</h1>\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "            </table>\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            </td>\n" +
                   "            </tr>\n" +
                   "            </table>\n" +
                   "            <![endif]-->\n" +
                   "        </td>\n" +
                   "    </tr>\n" +
                   "    <!-- end hero -->\n" +
                   "\n" +
                   "    <!-- start copy block -->\n" +
                   "    <tr>\n" +
                   "        <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                   "            <![endif]-->\n" +
                   "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                   "\n" +
                   "                <!-- start copy -->\n" +
                   "                <tr>\n" +
                   "                    <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                   "                        <p style=\"margin: 0;\"><b>Hi "+ firstname + ",</b></p>\n" +
                   "                        <p style=\"margin: 0;\">Welcome to church of Philippi. Please use this temporary Password to log in to our app.</p>\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "                <!-- end copy -->\n" +
                   "\n" +
                   "                <!-- start button -->\n" +
                   "                <tr>\n" +
                   "                    <td align=\"left\" bgcolor=\"#ffffff\">\n" +
                   "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                   "                            <tr>\n" +
                   "                                <td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 12px;\">\n" +
                   "                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                   "                                        <tr>\n" +
                   "                                            <td align=\"center\">\n" +
                   "                                                <p style=\"font-size: 40px; color: #565756; font-family: 'Trebuchet MS', sans-serif;\"> <b>"+password+"</b></p>\n" +
                   "                                            </td>\n" +
                   "                                        </tr>\n" +
                   "                                    </table>\n" +
                   "                                </td>\n" +
                   "                            </tr>\n" +
                   "                        </table>\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "                <!-- end button -->\n" +
                   "\n" +
                   "                <!-- start copy -->\n" +
                   "                <tr>\n" +
                   "                    <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 10px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                   "                        <p style=\"margin: 0;\">Download our application. Available on the App Store and Google Play</p>\n" +
                   "                      \n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center   \" bgcolor=\"#ffffff\" \">\n" +
                   "                        <a href=\"https://play.google.com/store\" style=\"text-decoration: none; text-align: center;\">\n" +
                   "                            <img src=\"https://i.ibb.co/Z1zHbZw/google-play-badge.png\" alt=\"play store\" style=\"width:180px;\"/>\n" +
                   "                        </a>\n" +
                   "                        <a href=\"https://www.apple.com/app-store/\" style=\"text-decoration: none;\">\n" +
                   "                            <img src=\"https://i.ibb.co/QJv7gF3/app-store.png\" alt=\"play store\" style=\"width:154px;height: 47px; margin-bottom: 11px;\" />\n" +
                   "                        </a>\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "                <!-- end copy -->\n" +
                   "\n" +
                   "                <!-- start copy -->\n" +
                   "                <tr>\n" +
                   "                    <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-bottom: 3px solid #d4dadf\">\n" +
                   "                        <p style=\"margin: 0;\">Thank you</p>\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "                <!-- end copy -->\n" +
                   "\n" +
                   "            </table>\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            </td>\n" +
                   "            </tr>\n" +
                   "            </table>\n" +
                   "            <![endif]-->\n" +
                   "        </td>\n" +
                   "    </tr>\n" +
                   "    <!-- end copy block -->\n" +
                   "\n" +
                   "    <!-- start footer -->\n" +
                   "    <tr>\n" +
                   "        <td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 24px;\">\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                   "            <![endif]-->\n" +
                   "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                   "\n" +
                   "                <!-- start permission -->\n" +
                   "\n" +
                   "                <!-- end permission -->\n" +
                   "\n" +
                   "                <!-- start unsubscribe -->\n" +
                   "                <tr>\n" +
                   "                    <td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;\">\n" +
                   "                        <p style=\"margin: 0;\">7422 Race Rd, Hanover, MD 21076 | 410-579-1555\u200B</p>\n" +
                   "                        <p style=\"margin: 0;\">© 2021 by Church of Philippi</p>\n" +
                   "                    </td>\n" +
                   "                </tr>\n" +
                   "                <!-- end unsubscribe -->\n" +
                   "\n" +
                   "            </table>\n" +
                   "            <!--[if (gte mso 9)|(IE)]>\n" +
                   "            </td>\n" +
                   "            </tr>\n" +
                   "            </table>\n" +
                   "            <![endif]-->\n" +
                   "        </td>\n" +
                   "    </tr>\n" +
                   "    <!-- end footer -->\n" +
                   "\n" +
                   "</table>\n" +
                   "<!-- end body -->\n" +
                   "\n" +
                   "</body>\n" +
                   "</html>";

        return res;
    }
}
