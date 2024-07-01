import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMyRoutesComponent } from './create-my-routes.component';

describe('CreateMyRoutesComponent', () => {
  let component: CreateMyRoutesComponent;
  let fixture: ComponentFixture<CreateMyRoutesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateMyRoutesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateMyRoutesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
