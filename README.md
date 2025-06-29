# Spring Boot Annotations with One-line Descriptions

## Core Annotations

### Application Configuration
```java
@SpringBootApplication         // Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
@EnableAutoConfiguration      // Automatically configures your Spring application based on dependencies
@ComponentScan               // Tells Spring where to look for components
@ConfigurationProperties     // Binds external configurations to Java objects
```

### Component Types
```java
@Component                   // Marks class as Spring-managed component
@Service                    // Indicates class is a service layer bean
@Repository                 // Marks class as data repository/DAO component
@Controller                 // Identifies class as Spring MVC controller
@RestController             // Combines @Controller and @ResponseBody for RESTful APIs
@Configuration             // Declares class as source of bean definitions
```

## Web Layer Annotations

### Request Mappings
```java
@RequestMapping             // Maps web requests to handler methods
@GetMapping                // Shortcut for @RequestMapping(method = GET)
@PostMapping               // Shortcut for @RequestMapping(method = POST)
@PutMapping                // Shortcut for @RequestMapping(method = PUT)
@DeleteMapping             // Shortcut for @RequestMapping(method = DELETE)
@PatchMapping              // Shortcut for @RequestMapping(method = PATCH)
```

### Request Parameters
```java
@RequestParam              // Binds web request parameters to method parameters
@PathVariable             // Extracts values from URI path
@RequestBody              // Binds HTTP request body to method parameter
@RequestHeader            // Maps HTTP header to method parameter
@CookieValue             // Binds cookie value to method parameter
```

## Data Layer Annotations

### JPA Annotations
```java
@Entity                    // Marks class as JPA entity
@Table                    // Specifies mapped database table
@Id                       // Marks field as primary key
@GeneratedValue           // Configures primary key generation strategy
@Column                   // Specifies mapped database column
@OneToMany                // Defines one-to-many relationship
@ManyToOne                // Defines many-to-one relationship
@JoinColumn              // Specifies foreign key column
```

## Security Annotations
```java
@PreAuthorize             // Defines access control expression before method execution
@PostAuthorize            // Verifies access after method execution
@Secured                  // Restricts method access based on roles
@RolesAllowed             // Java standard annotation for role-based access
```

## Bean Lifecycle
```java
@PostConstruct            // Method to execute after dependency injection
@PreDestroy               // Method to execute before bean destruction
@Scope                    // Defines bean scope (singleton, prototype, etc.)
@Lazy                     // Delays bean initialization until needed
@DependsOn               // Specifies bean initialization dependencies
@Order                    // Defines component loading order
```

## Configuration
```java
@Value                    // Injects property values into fields
@PropertySource           // Specifies property source location
@Profile                  // Conditionally enables beans based on active profiles
@Conditional             // Conditionally enables beans based on conditions
```

## Utility Annotations

### Caching
```java
@Cacheable               // Enables caching for method results
@CacheEvict              // Removes entries from cache
@CachePut                // Updates cache without affecting method execution
```

### Async & Scheduling
```java
@Async                    // Marks method for asynchronous execution
@Scheduled                // Configures method for scheduled execution
@EnableAsync              // Enables async method execution
@EnableScheduling         // Enables scheduled method execution
```

### Validation
```java
@Valid                    // Triggers validation on annotated element
@Validated                // Enables method validation
@NotNull                  // Validates element must not be null
@Size                     // Validates element size constraints
@Email                    // Validates email format
```

### Exception Handling
```java
@ControllerAdvice         // Defines global exception handler
@ExceptionHandler         // Handles specific exception types
@ResponseStatus           // Specifies HTTP status code for exceptions
```

### Transaction Management
```java
@Transactional           // Defines transaction boundaries
@EnableTransactionManagement // Enables Spring's transaction handling
```

### Testing
```java
@SpringBootTest           // Main annotation for Spring Boot testing
@WebMvcTest              // Tests Spring MVC components
@DataJpaTest             // Tests JPA components
@MockBean                // Creates and injects mock for bean
@AutoConfigureMockMvc    // Autoconfigure MockMvc