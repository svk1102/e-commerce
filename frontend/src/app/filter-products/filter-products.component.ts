import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component , ElementRef, ViewChild, inject , OnInit} from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import { Observable, map, retry, startWith } from 'rxjs';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { CategoryService } from '../services/category.service';
import { UserService } from '../services/user.service';
import { ProductService } from '../services/product.service';


@Component({
  selector: 'app-filter-products',
  templateUrl: './filter-products.component.html',
  styleUrls: ['./filter-products.component.css']
})
export class FilterProductsComponent implements OnInit{

  separatorKeysCodes: number[] = [ENTER, COMMA];
  categoryCtrl = new FormControl('');
  merchantCtrl = new FormControl('');
  filteredCategorys: Observable<any[]>;
  filteredMerchants: Observable<any[]>;
  categorys: any[] = [];
  allCategorys: any[] = [];
  categoryIds:any[] = [];
  merchantIds:any[] = [];
  merchants: any[] = [];
  allMerchants: any[] = [];
  tempMerchants:any;

  upperValue:number=50000
  lowerValue:number=0

  @ViewChild('categoryInput') categoryInput!: ElementRef<HTMLInputElement>;
  @ViewChild('merchantInput') merchantInput!: ElementRef<HTMLInputElement>;

  announcer = inject(LiveAnnouncer);

  constructor(private category:CategoryService , private user:UserService , private product:ProductService) {
    this.filteredCategorys = this.categoryCtrl.valueChanges.pipe(
      startWith(null),
      map((category: string | null) => (category ? this._filter(category) : this.allCategorys.slice())),
    );

    this.filteredMerchants = this.merchantCtrl.valueChanges.pipe(
      startWith(null),
      map((merchant: string | null) => (merchant ? this._filterMerchant(merchant) : this.allMerchants.slice())),
    );
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    // Add our category
    if (this.allCategorys.includes(value)) {
      this.categorys.push(value);
    }
    // Clear the input value
    event.chipInput!.clear();
    this.categoryCtrl.setValue(null);
  }

  addMerchant(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    // Add our category
    if (this.allMerchants.includes(value)) {
      this.merchants.push(value);
    }
    // Clear the input value
    event.chipInput!.clear();
    this.merchantCtrl.setValue(null);
  }

  remove(category: string): void {
    const index = this.categorys.indexOf(category);

    if (index >= 0) {
      this.categorys.splice(index, 1);

      this.announcer.announce(`Removed ${category}`);
      var id = this.getCategoryIdFromName(category)
      var spilceIndex = this.categoryIds.indexOf(id);
      this.categoryIds.splice(spilceIndex,1);
      // this.getFiteredProducts();
      this.getFilteredProductsBySingleQuery();
    }
  }

  removeMerchant(merchant: string): void {
    const index = this.merchants.indexOf(merchant);

    if (index >= 0) {
      this.merchants.splice(index, 1);

      this.announcer.announce(`Removed ${merchant}`);

      var id = this.getMerchantIdFromName(merchant)
      var spilceIndex = this.merchantIds.indexOf(id);
      this.merchantIds.splice(spilceIndex,1);
      // this.getFiteredProducts();
      this.getFilteredProductsBySingleQuery();
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.categorys.push(event.option.viewValue);
    this.categoryInput.nativeElement.value = '';
    this.categoryCtrl.setValue(null);
    var id = this.getCategoryIdFromName(event.option.viewValue)
    this.categoryIds.push(id);
    // this.getFiteredProducts();
    this.getFilteredProductsBySingleQuery();
    
  }

  selectedMerchant(event: MatAutocompleteSelectedEvent): void {
    this.merchants.push(event.option.viewValue);
    this.merchantInput.nativeElement.value = '';
    this.merchantCtrl.setValue(null);
    var id = this.getMerchantIdFromName(event.option.viewValue)
    this.merchantIds.push(id);
    // this.getFiteredProducts();
    this.getFilteredProductsBySingleQuery();
}

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allCategorys.filter(category => category.name.toLowerCase().includes(filterValue));
  }

  private _filterMerchant(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allMerchants.filter(merchant => merchant.toLowerCase().includes(filterValue));
  }

  getCategoryIdFromName(name:string):Number{
    var id!:number;
    this.allCategorys.forEach(c => {
      if(c.name == name){
        id = c.id;
      }
    })
    return id;
  }

  getMerchantIdFromName(name:string):Number{
    var id!:number;
    this.tempMerchants.forEach((m: { firstName: string; lastName: string; id: number; }) => {
      if(m.firstName + " " + m.lastName == name){
        id = m.id;
      }
    })
    return id;
  }

  // getFiteredProducts(){
  //   this.product.getFilteredProducts(this.merchantIds,this.categoryIds,this.upperValue,this.lowerValue).subscribe(res => {
  //     this.product.setFilterProducts(res);
  //   })
  // }

  getFilteredProductsBySingleQuery(){
    this.product.getFilteredProductsBySingleParam({
      categoryIds: this.categoryIds?.join(","),
      merchantIds: this.merchantIds?.join(","),
      minPrice: this.lowerValue,
      maxPrice: this.upperValue  
    }).subscribe(res => {
      this.product.setFilterProducts(res);
    })
  }

  formatLabel(value: number): string {
    if (value >= 1000) {
      return value / 1000 + 'k';
    }

    return `${value}`;
  }

  productFilter(){
    // this.getFiteredProducts();
    this.getFilteredProductsBySingleQuery();
  }

  ngOnInit(): void {
    this.category.getCategory().subscribe(res => {
      // res.forEach((c: { name: string; }) => {
      //   this.allCategorys.push(c.name);
      // })
      this.allCategorys = res;
    })

    this.user.getAllMerchants().subscribe(res => {
      this.tempMerchants = res;
      res.forEach((m: { firstName: string; lastName: string; }) => {
        this.allMerchants.push(m.firstName + " " + m.lastName);
      })
    })
  }
  

}
