# OnlineQuizApp
Мобильное приложение "Автошкола". Дипломная работа

Для выполнения задания были использованы следующие пакеты библиотек: Firebase, Lifecycle, Junit.

Сейчас реализованы следующие функции:
1. Регистрация(с потверждением email) и авторизация пользователя. 
2. Тестирование теории ПДД с подгрузкой данных из базы данных Firebase(все данные в базе данных, любой вопрос можно поменять, не меняя приложения и т.д.)
3. Запись на урок практики вождения. Ученику задаётся инструктор на собрании, после чего он может установить мобильное приложение и назначить занятие вождения выбравв дату и время. После успешной записи его запись занятия появляется в базе данных
Самое важное в этой функции реализована проверка введенных данных. Пример: если у инструктора есть запись занятия на 1 сентября в 8 утра с учеником A, то ученик B не может записаться на то же самое время, так как запись занята.
4. Созданы unit-тесты. 

Будущие функции которые предстоит добавить:
1. Улучшенные права инструктора, для того, чтобы он мог отменять записи, просматривать всех своих учеников и т.д.
2. Наполнение страницы "главная"
3. При необходимости добавить чат между учеником и инструктором
4. Реализовать авторизацию по смс

Исправить:
~~1. В тесте номер вопроса отстаёт на одно положение.~~
2. В тесте адаптировать большое количество текста чтобы не выходило за экран.
3. В тесте добавить обьяснение вопроса при неправильном ответе.



Скриншоты приложения 

<br>Страница авторизации: <br>
![Screenshot_1](https://user-images.githubusercontent.com/90863647/221120430-62102676-0983-4b08-b4c6-abccb11d0c99.png)

<br>Страница регистрации: <br>
![Screenshot_2](https://user-images.githubusercontent.com/90863647/221120509-340eeca7-dc1c-4b08-ae37-d53eef3ab629.png)

<br>Главная страница: <br>
![Screenshot_3](https://user-images.githubusercontent.com/90863647/221120576-3037c2ad-cb9f-4e0e-8d8d-e82f177997d1.png)

<br>Страница профиля:<br>
![Screenshot_4](https://user-images.githubusercontent.com/90863647/221120602-7c71667b-8e48-4212-bf69-1b192e4fd9be.png)

<br>Страница записи:<br>
![Screenshot_5](https://user-images.githubusercontent.com/90863647/221120641-8cea779c-4b05-4514-ba16-daac4de3fe7c.png)

<br>Страница записи с окном выбора времени:<br>
![Screenshot_6](https://user-images.githubusercontent.com/90863647/221120679-5b8b5719-cbb9-4207-a3d6-e1f6b5f8b40a.png)

<br>Страница записи с окном выбора даты:<br>
![Screenshot_7](https://user-images.githubusercontent.com/90863647/221120767-eece771f-bdca-4eb6-ab32-24648b4e1420.png)

<br>Страница уроков:<br>
![Screenshot_8](https://user-images.githubusercontent.com/90863647/221120823-a41b2310-400b-42d6-ae9a-a693573a45df.png)

<br>Страница выбора билетов для теста <br>
![image](https://user-images.githubusercontent.com/90863647/221383952-184f2fa8-c1e9-454c-90e9-91a8ae00cdde.png)

<br>Страница начала теста: <br>
![Screenshot_9](https://user-images.githubusercontent.com/90863647/221120880-b4a6c800-df93-4605-b22f-4f0fef3995e0.png)

<br>Страница прохождения вопросов теста с неправильным ответом:<br>
![Screenshot_10](https://user-images.githubusercontent.com/90863647/221120964-0c49a061-6de5-4ce8-a82d-76e39231db68.png)

<br>Страница прохождения вопросов теста с правильным ответом:<br>
![Screenshot_11](https://user-images.githubusercontent.com/90863647/221121010-b19d0796-1dfe-42ef-ae48-57006c2fc4d5.png)

<br>Страница результатов теста:<br>
![Screenshot_12](https://user-images.githubusercontent.com/90863647/221121478-a71a2546-dfb8-440d-aba1-cecf6a52ee42.png)






