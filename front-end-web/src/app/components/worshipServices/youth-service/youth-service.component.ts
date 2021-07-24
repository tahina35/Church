import { Component, OnInit } from '@angular/core';
import { NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Select2OptionData } from 'ng-select2';
import { Member } from 'src/app/model/Member';
import { Page } from 'src/app/model/Page';
import { YouthService } from 'src/app/model/YouthService';
import { WorshipService } from 'src/app/services/worship.service';
import { DeleteServiceModal } from '../delete-service.modal';
import { VideoPlayerModal } from '../video-player.modal';

@Component({
  selector: 'app-youth-service',
  templateUrl: './youth-service.component.html',
  styleUrls: ['./youth-service.component.scss']
})
export class YouthServiceComponent implements OnInit {

  totalPages:number;
  currentPage: number = 1;
  totalItems:number = 0;
  error?: string;
  youthService: YouthService = new YouthService();
  preachers: Array<Select2OptionData> = [];
  selectedDate: NgbDateStruct;
  services: YouthService[] = [];

  constructor(private modalService: NgbModal, private worshipService: WorshipService) { }

  ngOnInit(): void {
    this.getPreachers();
    this.getYouthServices();
  }

  getYouthServices() {
    this.worshipService.getYouthServices(this.currentPage).subscribe(
      (data: Page<YouthService>) => {
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

  saveOrUpdateYouthService() {
    if(!!this.youthService.sermon.url) {
      this.youthService.sermon.isUploaded = true;
    } else this.youthService.sermon.isUploaded = false;
    
    const date = this.selectedDate.year + '-' + this.selectedDate.month + '-' + this.selectedDate.day;
    this.youthService.date = new Date(date);
    this.worshipService.saveOrUpdateYouthService(this.youthService).subscribe(
      (service: YouthService) => {
        this.currentPage = 1;
        this.getYouthServices();
        this.youthService = new YouthService();
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
      this.youthService = new YouthService();
  }

  open(content) {
    this.modalService.open(content, { size: 'lg' }).result.then(
      () => {},
      (reason) => {
        this.resetModal(); 
      }
    )
  }

  update(content, service: YouthService) {
    let date = new Date(service.date);
    this.selectedDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() + 1};
    this.youthService = service;
    this.modalService.open(content, { size: 'lg' }).result.then(
      () => {},
      (reason) => {
        this.resetModal(); 
      }
    )
  }

  deleteService(service: YouthService) {
    console.log(service);
    this.modalService.open(DeleteServiceModal, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {
        this.worshipService.deleteYouthService(service.date).subscribe(
          () => {
            this.currentPage = 1;
            this.getYouthServices();
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
    this.getYouthServices();
  }

  playSermon(service: YouthService) {
    const modalRef = this.modalService.open(VideoPlayerModal, {size: 'lg'});
    modalRef.componentInstance.title = 'Youth Service (' + service.date + ')';
    modalRef.componentInstance.id = this.worshipService.getVideoId(service.sermon.url);
  }

}
