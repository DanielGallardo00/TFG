import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyUserFavsComponent } from './my-user-favs.component';

describe('MyUserFavsComponent', () => {
  let component: MyUserFavsComponent;
  let fixture: ComponentFixture<MyUserFavsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyUserFavsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyUserFavsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
