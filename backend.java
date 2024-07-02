// Product.java (Entity)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    // Getters and Setters
}

// ProductRepository.java (Repository)
public interface ProductRepository extends JpaRepository<Product, Long> {
}

// ProductService.java (Service)
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProductQuantity(Long id, int quantity) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setQuantity(quantity);
        return productRepository.save(product);
    }
}

// ProductController.java (Controller)
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public Product updateProductQuantity(@PathVariable Long id, @RequestBody int quantity) {
        return productService.updateProductQuantity(id, quantity);
    }
}
Frontend (Angular):

typescript
Copiar código
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