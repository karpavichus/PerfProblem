## Что нужно сделать:

- написать тестовые сценарии текстом
- написать авто-тесты на эти сценарии
- настроить любой CI/CD для запуска тестов
- далее надо попытаться понять, какие есть перфоманс проблемы в этом решении. Для этого нужно использовать YourKit или любой другой профилятор.

### По результатам профиляции нужно:

- создать несколько перформанс-тикетов с приложенными снепшотами
- попытаться разрешить найденные перфоманс проблемы и покрыть решения performance тестами

## Сделано

- Тестовые сценарии в файле [TestCases.md](./TestCases.md)
- Авто-тесты в [GeneralTest.java](./src/test/java/GeneralTest.java)
- Перформанс проблемы/тикеты описаны в [PerformanceTickets.md](./performanceTickets/PerformanceTickets.md)
- Найденные проблемы разрешены в копии оринальной программы [PerfProblemAfterFixes.java](./src/main/java/PerfProblemAfterFixes.java) 
- В оригинальной программе добавлены комментарии [PerfProblem.java](./src/main/java/PerfProblem.java)
- Перформанс тесты в [PerformanceTest.java](./src/test/java/PerformanceTest.java)
- GitHub репозиторий [GitHub](https://github.com/karpavichus/PerfProblem)
- CI/CD в [TeamCity](https://perf4jb.teamcity.com/project/PerfProblem) _PS: логин и пароль вы сможете найти в письме_