import { Component, OnInit } from '@angular/core';
import { NgbDate, NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Select2OptionData } from 'ng-select2';
import { Member } from 'src/app/model/Member';
import { Page } from 'src/app/model/Page';
import { SundayService } from 'src/app/model/SundayService';
import { WorshipService } from 'src/app/services/worship.service';
import { environment } from 'src/environments/environment';
import { DeleteServiceModal } from '../delete-service.modal';
import { VideoPlayerModal } from '../video-player.modal';

@Component({
  selector: 'app-sunday-service',
  templateUrl: './sunday-service.component.html',
  styleUrls: ['./sunday-service.component.scss']
})
export class SundayServiceComponent implements OnInit {

  totalPages:number;
  currentPage: number = 1;
  totalItems:number = 0;
  error?: string;
  serviceTypes: any = environment.sundayServiceType;
  sundayService: SundayService = new SundayService();
  preachers: Array<Select2OptionData> = [];
  prayer: Array<Select2OptionData> = [];
  selectedDate: NgbDateStruct;
  services: SundayService[] = [];

  constructor(private modalService: NgbModal, private worshipService: WorshipService) { }

  ngOnInit(): void {
    this.getPreachers();
    this.getSundayServices();
  }

  getSundayServices() {
    this.worshipService.getSundayServices(this.currentPage).subscribe(
      (data: Page<SundayService>) => {
        console.log(data.content)
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
        const members = data.map(
          (preacher: Member) => {
            return ({id: preacher.memberId.toString(), text: preacher.kfname + ' ' + preacher.klname});
          }
        );
        this.preachers = members;
      },
      (err) => {
        this.error = err;
      }
    );
  }

  saveOrUpdateSundayService() {
    if(!!this.sundayService.sermon.url) {
      this.sundayService.sermon.isUploaded = true;
    } else this.sundayService.sermon.isUploaded = false;

    const date = this.selectedDate.year + '-' + this.selectedDate.month + '-' + this.selectedDate.day;
    this.sundayService.date = new Date(date);
    this.worshipService.saveOrUpdateSundayService(this.sundayService).subscribe(
      (service: SundayService) => {
        this.currentPage = 1;
        this.getSundayServices();
        this.sundayService = new SundayService();
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
      this.sundayService = new SundayService();
  }

  open(content) {
    this.modalService.open(content, { size: 'lg' }).result.then(
      () => {},
      (reason) => {
        this.resetModal();  
      }
    )
  }

  update(content, service: SundayService) {
    let date = new Date(service.date);
    this.selectedDate = {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() + 1};
    this.sundayService = service;
    this.modalService.open(content, { size: 'lg' }).result.then(
      () => {},
      (reason) => {
        this.resetModal();  
      }  
    )
  }

  deleteService(service: SundayService) {
    console.log(service);
    this.modalService.open(DeleteServiceModal, {ariaLabelledBy: 'modal-basic-title'}).result.then(
      () => {
        this.worshipService.deleteSundayService(service.date, service.serviceType).subscribe(
          () => {
            this.currentPage = 1;
            this.getSundayServices();
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
    this.getSundayServices();
  }

  playSermon(service: SundayService) {
    const modalRef = this.modalService.open(VideoPlayerModal, {size: 'lg'});
    modalRef.componentInstance.title = this.worshipService.getServiceType(service.serviceType) + '  (' + service.date + ') ';
    modalRef.componentInstance.id = this.worshipService.getVideoId(service.sermon.url);
  }

}
