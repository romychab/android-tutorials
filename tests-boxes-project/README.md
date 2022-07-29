# Tests Example

- This is a project from [this video tutorial](https://youtu.be/YH4dP94giJ0)
- Some changes has been made in this project since the publication of Hilt tutorial in order to make the project more testable:
  - refactored the project structure: introduced `data`, `domain` and `presentation` packages
  - replaced some inline methods (inline methods can't be mocked)
  - added factories: `LazyFlowFactory` and `LazyListenersFactory` for creating instances of `LazyFlowSubject` and `LazyListenersSubject` respectively
  - minor refactoring of `BaseViewModel` and `ProfileViewModel`/`ProfileFragment`

Checklist:
- [x] Prepare the project
- [x] Unit tests for data sources
- [ ] Unit tests for repositories
- [ ] Unit tests for view models
