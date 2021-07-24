import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReimbursementMainComponent } from './reimbursement-main.component';

describe('ReimbursementMainComponent', () => {
  let component: ReimbursementMainComponent;
  let fixture: ComponentFixture<ReimbursementMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReimbursementMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReimbursementMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
