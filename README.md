## Проектная работа
### Тема: "Распределенная Электронно-цифровая подпись"

Цель: Создать сервис с возможностью подписи документа на рабочем месте с физического ключа (токена) и с удаленного хранилища ключей

Идея состоит в том, что имеется центральный сервис подписи документа. В случае, если на службе есть необходимость работы с токенами, то локально на необходимых компьютерах ставится локальный сервис. Каждый локальный сервис должен иметь своё уникальное имя.

При подключении токена, локальный сервис передаёт на центральный информацию о подключенном токене.

При получении запроса на подпись центральный сервис сначала проверяет центральное хранилище на наличие данных о пользователе достаточных для подписи. В случае отсутствия данных находится сервис, в котором на данный момент подключен токен пользователя и перенаправляет запрос ему.

### Что использовалось:

Распределенная ЭЦП это набор сервисов написанных на Spring Boot с использованием Eureka Server(Discovery Server). В качестве средства криптографической защиты информации 
использовался КриптоПро JCP 2.0. В качестве физических токенов использовались Рутокены (модели: Lite и ЭЦП 2.0) российского производства компании «Актив». 

#### Пример Rest Api на ЭЦП с удаленного хранилища ключей

```
curl -i -X POST \
   -H "Content-Type:multipart/form-data" \
   -H "Authorization:Basic dXNlcjE6MTIzNDU=" \
   -F "file=@\"./{filename}\";type=application/pdf;filename=\"{filename}\"" \
   -F "pwd=$2a$10$8XFMuc1EZB1OHlAkjJ.2AuAvz96TZWXEIp/.6iUJtP7SIRQ5be/Tu" \
 'http://{host}:{port}/sign/{id}'
 ```

- Параметр pwd - пароль к приватному ключу в удаленном хранилище. Передается в зашифрованном виде.
- Параметр file - документ который необходимо подписать
#### Пример Rest Api на ЭЦП с физического хранилища ключей (токена)

```
curl -i -X POST \
   -H "Content-Type:multipart/form-data" \
   -H "Authorization:Basic dXNlcjE6MTIzNDU=" \
   -F "file=@\"./{filename}\";type=application/pdf;filename=\"{filename}\"" \
 'http://{host}:{port}/sign/{id}'
 ```
 
в ответ возвращается строка ЭЦП в кодировке Base64.
 

Итоги:
Реализованно 3 модуля.
1. **server-discovery** - стандартный Eureka Server
2. **central-sign-service** - удаленный сервис ЭЦП
3. **local-sign-service** - локальный сервис ЭЦП (для подписи с физического токена)

 
