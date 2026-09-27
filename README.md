# Лабораторна робота №2. Розроблення об’єктної моделі застосунку

Стартова заготовка TaskFlow. Потрібні базові знання класів, методів, інтерфейсів і умов Java; результат ЛР1 не потрібен.

## Середовище й запуск

JDK 25 (перевірено Temurin 25.0.4.1), Maven 3.9.16 через наданий Wrapper, JUnit Jupiter 6.0.3, Git 2.x. Перший запуск потребує інтернету. Установіть JDK 25, налаштуйте JAVA_HOME на каталог JDK та додайте його bin у PATH. IDE довільна.

Команди PowerShell з кореня проєкту:

```powershell
java -version
javac -version
git --version
.\mvnw.cmd -version
.\mvnw.cmd clean test
.\mvnw.cmd compile
java -cp target/classes ua.khnu.kpp.Counter
java -cp target/classes ua.khnu.kpp.App
```

У Linux/macOS замініть `.\mvnw.cmd` на `sh ./mvnw`.
Початкова заготовка компілюється: 14 тестів, 2 успішні, 1 невдала перевірка, 11 помилок, 0 пропусків. Причина — нереалізовані методи предметної моделі з UnsupportedOperationException. Counter друкує `3`, App спочатку друкує `Lab 2: domain model exercise`. Приклад Counter повністю реалізований та перевірений двома тестами.

## Завдання та дозволені зміни

Реалізуйте контракт User, Project і Task з методичної інструкції ЛР2; перехід NEW → ACTIVE потребує виконавця, ACTIVE → DONE — коректних доказів і дозволу CompletionPolicy. Відмова не змінює стан. Дата завершення не раніша за створення; прострочене завершення дозволене. Неправильний стан перевіряють до аргументів. Некоректні аргументи спричиняють IllegalArgumentException, стан або відмова правила — IllegalStateException.

Дозволено змінювати лише такі файли: `src/main/java/ua/khnu/kpp/User.java`, `Project.java`, `Task.java`, `VariantPolicy.java`, `App.java` у тому самому пакеті, `variant.txt` і цей README.md. Додайте власний `report.pdf`. Інші файли незмінні. Номер у variant.txt має відповідати призначеному викладачем (початково 1). Реалізуйте лише правило свого номера у VariantPolicy; 224 комбінації для вибраного номера перевіряє один незмінний тест. Правила та 10 варіантів наведено в PDF ЛР2.

Заборонено редагувати, видаляти, перейменовувати, вимикати або обходити тести, тестові дані чи змінювати налаштування складання. Не підміняйте вивід. Після реалізації всі 14 тестів повинні пройти без пропусків:

```powershell
.\mvnw.cmd clean verify
java -cp target/classes ua.khnu.kpp.App
```

Фінальний App має демонструвати реальні стани NEW, ACTIVE, відмову без зміни ACTIVE, потім DONE. Додайте до README опис власного варіанта, схему відповідностей предметній області, значення аргументів і фактичний вивід App.

## Незмінність тестів

Залиште незалежну копію наданої заготовки в сусідній папці `lab-2-teacher`. Із робочої копії виконайте:

```powershell
git diff --no-index --exit-code ../lab-2-teacher/src/test src/test
git diff --no-index --exit-code ../lab-2-teacher/pom.xml pom.xml
git diff --no-index --exit-code ../lab-2-teacher/.mvn .mvn
git diff --no-index --exit-code ../lab-2-teacher/mvnw mvnw
git diff --no-index --exit-code ../lab-2-teacher/mvnw.cmd mvnw.cmd
```

Порожній вивід і код 0 після кожної команди означають збіг; 1 — відмінності, інший код — помилку. Так само порівняйте .gitignore, Status.java, CompletionPolicy.java, Counter.java за їхніми шляхами. Викладач запускає реалізацію зі своєю незмінною копією тестів. Змінені, вилучені, вимкнені чи обійдені тести означають, що робота не приймається.

## Подання через Git

Працюйте у власній копії й гілці `lab-2`: `git init -b lab-2`. Потрібні щонайменше три змістовні коміти (заготовка, сутності, модель і докази). Збережіть вихідний код, тести, файли складання, .gitignore, README.md та report.pdf. Не додавайте секрети, токени, локальні облікові дані, кеші, target або файли IDE.

Створіть порожній віддалений репозиторій, надайте викладачу доступ і виконайте після комітів:

```powershell
$repoUrl = Read-Host 'Repository URL'
git remote add origin $repoUrl
git push -u origin lab-2
git remote get-url origin
git rev-parse HEAD
```

Подайте URL, гілку lab-2 і повний commit SHA; оцінюється саме цей коміт. Перевірте відтворення в новій папці: `git clone --branch lab-2` із фактичною адресою, `git checkout --detach` із поданим SHA, потім `clean verify` і запуск App наведеними командами. Без завантаженого доступного викладачу коміту ЛР не вважається поданою. Звіт має містити схему, сценарії з фактичними результатами, аналіз граничних випадків і висновок.

## Документація

[Java 25](https://docs.oracle.com/en/java/javase/25/docs/api/), [Maven 3.9.16](https://maven.apache.org/docs/3.9.16/release-notes.html), [JUnit 6.0.3](https://docs.junit.org/6.0.3/overview.html), [Git diff](https://git-scm.com/docs/git-diff). Перевірено 09.09.2026.
