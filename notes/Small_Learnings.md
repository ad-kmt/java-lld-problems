- Use final for immutable fields
- Use specific exception for more granular exception handling
- Don't forget to validate arguments/ parameters
- For printing formatted information use System.out.printf()
- Ensuring that the repository is the source of truth for any data helps maintain consistency, especially in a concurrent environment.
  - e.g. using id as a parameter in service layer to get latest data from repository instead of taking data model as a parameter from presentation layer.


### Returning null vs throwing exception.
Returning null can be ambiguous and may not provide sufficient context about what went wrong. In such cases, using exceptions to signal specific issues can improve the clarity and robustness of your application.

To enhance the design, we can:
- Throw Specific Exceptions from the Repository Layer: This approach provides more detailed error information directly from the repository.
- Catch and Wrap Exceptions in the Service Layer: The service layer can catch these exceptions and wrap them in higher-level exceptions if necessary.

### Validation

- Centralize General Validation Logic: Use a utility class for common validation tasks.
- Service-Specific Validators: Create validators for each service that handle specific validation rules for that domain.
