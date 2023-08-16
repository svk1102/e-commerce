import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbSlideEvent, NgbSlideEventSource } from '@ng-bootstrap/ng-bootstrap';
import { NgbCarousel} from '@ng-bootstrap/ng-bootstrap';
import { ProductService } from '../services/product.service';


@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent implements OnInit{
	images = [62, 83, 466, 965, 982, 1043, 738].map((n) => `https://picsum.photos/id/${n}/900/500`);

	paused = false;
	unpauseOnArrow = false;
	pauseOnIndicator = false;
	pauseOnHover = true;
	pauseOnFocus = true;
  likedProducts:any;
  carouselProducts:any[] = [];

	@ViewChild('carousel', { static: true }) carousel!: NgbCarousel;

  constructor(private product:ProductService){}


	togglePaused() {
		if (this.paused) {
			this.carousel.cycle();
		} else {
			this.carousel.pause();
		}
		this.paused = !this.paused;
	}

	onSlide(slideEvent: NgbSlideEvent) {
		if (
			this.unpauseOnArrow &&
			slideEvent.paused &&
			(slideEvent.source === NgbSlideEventSource.ARROW_LEFT || slideEvent.source === NgbSlideEventSource.ARROW_RIGHT)
		) {
			this.togglePaused();
		}
		if (this.pauseOnIndicator && !slideEvent.paused && slideEvent.source === NgbSlideEventSource.INDICATOR) {
			this.togglePaused();
		}
	}

  getLikedProduct(){
    this.product.getAllProducts().subscribe(res => {
      this.likedProducts = res;
      this.likedProducts[this.likedProducts.length-1].title = "View the most recently added product"; //adding this to the most recently added product to the array
      this.carouselProducts.push(this.likedProducts[this.likedProducts.length-1])
      this.likedProducts.sort((a: { likes: string | any[]; },b: { likes: string | any[]; }) => b.likes.length - a.likes.length)
      this.likedProducts[0].title = "View the most liked product of all!" //adding this to the most liked product of array to the sorted list of arrays
      this.carouselProducts.push(this.likedProducts[0]);
      this.likedProducts.sort((a: { price: number; },b: { price: number; }) => b.price - a.price);
      this.likedProducts[0].title = "Our Sponsored product | Deal of the day"
      this.carouselProducts.push(this.likedProducts[0])
      console.log(this.carouselProducts);
      // console.log(this.likedProducts)
    })
  }

  ngOnInit(): void {
    this.getLikedProduct();
  }

}
