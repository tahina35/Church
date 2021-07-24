import { Component, OnInit } from '@angular/core';
import { NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Select2OptionData } from 'ng-select2';
import { Member } from 'src/app/model/Member';
import { MorningService } from 'src/app/model/MorningService';
import { Page } from 'src/app/model/Page';
import { WorshipService } from 'src/app/services/worship.service';
import { DeleteServiceModal } from '../delete-service.modal';
import { VideoPlayerModal } from '../video-player.modal';

@Component({
  selector: 'app-morning-service',
  templateUrl: './morning-service.component.html',
  styleUrls: ['./morning-service.component.scss']
})
export class MorningServiceComponent implements OnInit {

  totalPages:number;
  currentPage: number = 1;
  totalItems:number = 0;
  error?: string;
  prayerMeeting: MorningService = new MorningService();
  preachers: Array<Select2OptionData> = [];
  selectedDate: NgbDateStruct;
  services: MorningService[] = [];

  constructor(private modalService: NgbModal, private worshipService: WorshipService) { }

  ngOnInit(): void {
    this.getPreachers();
    this.getMorningServices();
  }

  getMorningServices() {
    this.worshipService.getMorningServices(this.currentPage).subscribe(
      (data: Page<MorningService>) => {
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.services = data.content;
      },
      (err) => {
        this.error = err;
      }
    )
  }

  getPreachers() {
    this.worshipService.getPreachers().subscribe(
      (data: Member[]) => {
        this.preachers = data.map(
          (preacher: Member) => {
            return ({id: preacher.memberId.toString(), text: preacher.kfname + ' ' + preacher.klname});
          }
        );
      },
      (err) => {
        this.error = err;
      }
    );
  }

  saveOrUpdateMorningService() {
    if(!!this.prayerMeeting.sermon.url) {
      this.prayerMeeting.sermon.isUploaded = true;
    } else this.prayerMeeting.sermon.isUploaded = false;

    const date = this.selectedDate.year + '-' + this.selectedDate.month + '-' + this.selectedDate.day;
    this.prayerMeeting.date = new Date(date);
    this.worshipService.saveOrUpdateMorningService(this.prayerMeeting).subscribe(
      (service: MorningService) => {
        this.currentPage = 1;
        this.getMorningServices();
        this.prayerMeeting = new MorningService();
        this.selectedDate = null;
        this.modalService.dismissAll();
      },
      (err) => {
        this.error = err;
      }
    );
  }

  resetModal() {
      this.selectedDate = null;
      this.prayerMeeting = new MorningService();
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {},
      (reason) => {
        this.resetModal();  
      }
    )
  }

  update(content, service: MorningService) {
    let date = new Date(service.date);
    this.selectedDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() + 1};
    this.prayerMeeting = service;
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {},
      (reason) => {
        this.resetModal();  
      }
    )
  }

  deleteService(service: MorningService) {
    console.log(service);
    this.modalService.open(DeleteServiceModal, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {
        this.worshipService.deleteMorningService(service.date).subscribe(
          () => {
            this.currentPage = 1;
            this.getMorningServices();
          },
          (err) => {
            this.error = err;
            setTimeout(()=>{                           //<<<---using ()=> syntax
              this.error = '';
            }, 8000);
          }
        )
      }
    )
  }

  onPageChange() {
    this.getMorningServices();
  }

  playSermon(service: MorningService) {
    const modalRef = this.modalService.open(VideoPlayerModal, {size: 'lg'});
    modalRef.componentInstance.title = 'Early Morning Service (' + service.date + ')';
    modalRef.componentInstance.id = this.worshipService.getVideoId(service.sermon.url);
  }

}
