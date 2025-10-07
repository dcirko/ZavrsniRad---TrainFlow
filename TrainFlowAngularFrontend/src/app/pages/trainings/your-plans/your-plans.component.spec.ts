import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YourPlansComponent } from './your-plans.component';

describe('YourPlansComponent', () => {
  let component: YourPlansComponent;
  let fixture: ComponentFixture<YourPlansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YourPlansComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YourPlansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
