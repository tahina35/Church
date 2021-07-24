import { Component, OnInit } from '@angular/core';
import { NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Select2OptionData } from 'ng-select2';
import { Member } from 'src/app/model/Member';
import { Page } from 'src/app/model/Page';
import { WednesdayService } from 'src/app/model/WednesdayService';
import { WorshipService } from 'src/app/services/worship.service';
import { DeleteServiceModal } from '../delete-service.modal';
import { VideoPlayerModal } from '../video-player.modal';

@Component({
  selector: 'app-wednesday-service',
  templateUrl: './wednesday-service.component.html',
  styleUrls: ['./wednesday-service.component.scss']
})
export class WednesdayServiceComponent implements OnInit {

  totalPages:number;
  currentPage: number = 1;
  totalItems:number = 0;
  error?: string;
  wednesdayService: WednesdayService = new WednesdayService();
  preachers: Array<Select2OptionData> = [];
  selectedDate: NgbDateStruct;
  services: WednesdayService[] = [];

  constructor(private modalService: NgbModal, private worshipService: WorshipService) { }

  ngOnInit(): void {
    this.getPreachers();
    this.getWednesdayServices();
  }

  getWednesdayServices() {
    this.worshipService.getWednesdayServices(this.currentPage).subscribe(
      (data: Page<WednesdayService>) => {
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

  saveOrUpdateWednesdayService() {
    if(!!this.wednesdayService.sermon.url) {
      this.wednesdayService.sermon.isUploaded = true;
    } else this.wednesdayService.sermon.isUploaded = false;
    
    const date = this.selectedDate.year + '-' + this.selectedDate.month + '-' + this.selectedDate.day;
    this.wednesdayService.date = new Date(date);
    this.worshipService.saveOrUpdateWednesdayService(this.wednesdayService).subscribe(
      (service: WednesdayService) => {
        this.currentPage = 1;
        this.getWednesdayServices();
        this.wednesdayService = new WednesdayService();
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
      this.wednesdayService = new WednesdayService();
  }

  open(content) {
    this.modalService.open(content, { size: 'lg' }).result.then(
      () => {},
      (reason) => {
        this.resetModal();  
      }
    )
  }

  update(content, service: WednesdayService) {
    let date = new Date(service.date);
    this.selectedDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() + 1};
    this.wednesdayService = service;
    this.modalService.open(content, { size: 'lg' }).result.then(
      () => {},
      (reason) => {
        this.resetModal();  
      }
    )
  }

  deleteService(service: WednesdayService) {
    console.log(service);
    this.modalService.open(DeleteServiceModal, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {
        this.worshipService.deleteWednesdayService(service.date).subscribe(
          () => {
            this.currentPage = 1;
            this.getWednesdayServices();
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
    this.getWednesdayServices();
  }

  playSermon(service: WednesdayService) {
    const modalRef = this.modalService.open(VideoPlayerModal, {size: 'lg'});
    modalRef.componentInstance.title = 'Wednesday Service (' + service.date + ')';
    modalRef.componentInstance.id = this.worshipService.getVideoId(service.sermon.url);
  }

}
