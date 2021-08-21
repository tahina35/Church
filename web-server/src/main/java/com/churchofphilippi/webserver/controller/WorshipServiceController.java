package com.churchofphilippi.webserver.controller;

import com.churchofphilippi.webserver.config.PageConfig;
import com.churchofphilippi.webserver.model.EarlyMorningServices;
import com.churchofphilippi.webserver.model.SundayService;
import com.churchofphilippi.webserver.model.WednesdayService;
import com.churchofphilippi.webserver.model.YouthService;
import com.churchofphilippi.webserver.model.keys.SundayServicekey;
import com.churchofphilippi.webserver.model.pagination.CustomPage;
import com.churchofphilippi.webserver.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/worship")
public class WorshipServiceController {

    private final SundayServiceService sundayServiceService;
    private final WednesdayServiceService wednesdayServiceService;
    private final EarlyMorningServicesService earlyMorningServicesService;
    private final YouthServiceService youthServiceService;
    private final AllMemberService memberService;
    private final PageConfig pageConfig;

    @GetMapping("/preachers")
    public ResponseEntity<?> getPreachers() {
        return ResponseEntity.ok(memberService.getPreachers());
    }

    // Sunday service operations

    @GetMapping("/sunday-service/page/{pageNo}")
    public ResponseEntity<?> getSundayServices(@PathVariable("pageNo")int pageNo) {
        Page<SundayService> paginated = sundayServiceService.findAllPaginated(pageNo, pageConfig.getSize());
        CustomPage<SundayService> page = new CustomPage<SundayService>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/sunday-service/last")
    public ResponseEntity<?> getLastSundayService() {
        return ResponseEntity.ok(sundayServiceService.findLast());
    }

    @PostMapping("/sunday-service")
    public ResponseEntity<?> addSundayService(@RequestBody SundayService service) {
        return ResponseEntity.ok(sundayServiceService.save(service));
    }

    @DeleteMapping("/sunday-service/delete")
    public ResponseEntity<?> deleteSundayService(@RequestParam(name = "date") String date, @RequestParam(name = "serviceType") int serviceType) {
        SundayServicekey id = new SundayServicekey(serviceType, LocalDate.parse(date));
        sundayServiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    // wednesday service operations

    @GetMapping("/wednesday-service/page/{pageNo}")
    public ResponseEntity<?> getWednesdayServices(@PathVariable("pageNo")int pageNo) {
        Page<WednesdayService> paginated = wednesdayServiceService.findAllPaginated(pageNo, pageConfig.getSize());
        CustomPage<WednesdayService> page = new CustomPage<WednesdayService>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/wednesday-service/last")
    public ResponseEntity<?> getLastWednesdayService() {
        return ResponseEntity.ok(wednesdayServiceService.findLast());
    }

    @PostMapping("/wednesday-service")
    public ResponseEntity<?> addWednesdayService(@RequestBody WednesdayService service) {
        return ResponseEntity.ok(wednesdayServiceService.save(service));
    }

    @DeleteMapping("/wednesday-service/delete")
    public ResponseEntity<?> deleteWednesdayService(@RequestParam(name = "date") String date) {
        LocalDate serviceDate = LocalDate.parse(date);
        wednesdayServiceService.deleteByDate(serviceDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // youth service operations

    @GetMapping("/youth-service/page/{pageNo}")
    public ResponseEntity<?> getYouthServices(@PathVariable("pageNo")int pageNo) {
        Page<YouthService> paginated = youthServiceService.findAllPaginated(pageNo, pageConfig.getSize());
        CustomPage<YouthService> page = new CustomPage<YouthService>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/youth-service/last")
    public ResponseEntity<?> getLastYouthService() {
        return ResponseEntity.ok(youthServiceService.findLast());
    }

    @PostMapping("/youth-service")
    public ResponseEntity<?> addYouthService(@RequestBody YouthService service) {
        return ResponseEntity.ok(youthServiceService.save(service));
    }

    @DeleteMapping("/youth-service/delete")
    public ResponseEntity<?> deleteYouthService(@RequestParam(name = "date") String date) {
        LocalDate serviceDate = LocalDate.parse(date);
        youthServiceService.deleteByDate(serviceDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // early morning service operations

    @GetMapping("/morning-service/page/{pageNo}")
    public ResponseEntity<?> getMorningServices(@PathVariable("pageNo")int pageNo) {
        Page<EarlyMorningServices> paginated = earlyMorningServicesService.findAllPaginated(pageNo, pageConfig.getSize());
        CustomPage<EarlyMorningServices> page = new CustomPage<EarlyMorningServices>(pageNo, paginated.getTotalPages(), paginated.getTotalElements(), paginated.getContent());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/morning-service/last5")
    public ResponseEntity<?> getLast5() {
        return ResponseEntity.ok(earlyMorningServicesService.findLast5());
    }

    @PostMapping("/morning-service")
    public ResponseEntity<?> addMorningService(@RequestBody EarlyMorningServices service) {
        return ResponseEntity.ok(earlyMorningServicesService.save(service));
    }

    @DeleteMapping("/morning-service/delete")
    public ResponseEntity<?> deleteMorningService(@RequestParam(name = "date") String date) {
        LocalDate serviceDate = LocalDate.parse(date);
        earlyMorningServicesService.deleteByDate(serviceDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
