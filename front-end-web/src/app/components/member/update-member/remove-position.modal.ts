import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Role } from 'src/app/model/Role';

@Component({
  selector: 'remove-from-dept-modal',
  template: `
  <div class="modal-header">
    <h4 class="modal-title" id="modal-title">Remove position</h4>
    <button type="button" class="close" aria-describedby="modal-title" (click)="modal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body">
    <p><strong>Are you sure you want to remove <i>{{role.position.name}}</i> position for member 
    <span class="text-primary">"{{role.member.kfname}} {{role.member.klname}}"</span>?</strong></p>
  </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-outline-secondary" (click)="modal.dismiss('cancel click')">Cancel</button>
    <button type="button" class="btn btn-danger" (click)="modal.close('Ok click')">Ok</button>
  </div>
  `
})
export class RemovePosition {

  @Input() role: Role;

  
  constructor(public modal: NgbActiveModal) {}
}