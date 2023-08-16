import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFiltersComponent } from './admin-filters.component';

describe('AdminFiltersComponent', () => {
  let component: AdminFiltersComponent;
  let fixture: ComponentFixture<AdminFiltersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminFiltersComponent]
    });
    fixture = TestBed.createComponent(AdminFiltersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
