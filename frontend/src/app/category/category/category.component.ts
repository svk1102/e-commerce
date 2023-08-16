import { Component , OnInit} from '@angular/core';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit{

  categories:any[] = [];

  constructor(private category:CategoryService){}

  getCategories(){
    this.category.getCategory().subscribe(res => {
      this.categories = res
    })
  }
  
  ngOnInit(): void {
   this.getCategories(); 
  }
}
