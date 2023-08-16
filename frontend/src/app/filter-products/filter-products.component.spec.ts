import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterProductsComponent } from './filter-products.component';

describe('FilterProductsComponent', () => {
  let component: FilterProductsComponent;
  let fixture: ComponentFixture<FilterProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilterProductsComponent]
    });
    fixture = TestBed.createComponent(FilterProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
