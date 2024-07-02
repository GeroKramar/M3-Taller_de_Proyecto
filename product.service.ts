// product.service.ts
@Injectable({
    providedIn: 'root'
  })
  export class ProductService {
    private apiUrl = 'http://localhost:8080/api/products';
  
    constructor(private http: HttpClient) {}
  
    getProducts(): Observable<Product[]> {
      return this.http.get<Product[]>(this.apiUrl);
    }
  
    updateProductQuantity(id: number, quantity: number): Observable<Product> {
      return this.http.put<Product>(`${this.apiUrl}/${id}`, { quantity });
    }
  }
  
  // product-list.component.ts
  @Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html'
  })
  export class ProductListComponent implements OnInit {
    products: Product[] = [];
  
    constructor(private productService: ProductService) {}
  
    ngOnInit(): void {
      this.productService.getProducts().subscribe(data => {
        this.products = data;
      });
    }
  
    updateQuantity(product: Product, newQuantity: number): void {
      this.productService.updateProductQuantity(product.id, newQuantity).subscribe(updatedProduct => {
        product.quantity = updatedProduct.quantity;
      });
    }
  }
  