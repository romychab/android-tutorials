# Tests Example

- This is a project from the following video tutorials:
  - [Unit tests for data sources](https://youtu.be/YH4dP94giJ0)
  - [Unit tests for domain](https://youtu.be/vpiVlpPr6nk)
- Some changes has been made in this project since the publication of Hilt tutorial in order to make the project more testable:
  - refactored the project structure: introduced `data`, `domain` and `presentation` packages
  - replaced some inline methods (inline methods can't be mocked)
  - added factories: `LazyFlowFactory` and `LazyListenersFactory` for creating instances of `LazyFlowSubject` and `LazyListenersSubject` respectively
  - minor refactoring of `BaseViewModel` and `ProfileViewModel`/`ProfileFragment`

Checklist:
- [x] Prepare the project
- [x] Unit tests for data sources
- [x] Unit tests for repositories
- [x] Unit tests for view models
