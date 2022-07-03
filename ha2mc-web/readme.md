## Exemple de curl

Permet de simuler un envoi de formulaire Hello Asso

formSlug et organizationSlug à ajuster

```
curl --location --request POST 'http://localhost:8080/helloasso/payment' \
--header 'Content-Type: application/json' \
--data-raw '{"data": {  "payer": {    "dateOfBirth": "1971-01-28T00:00:00+01:00",    "email": "mail@mail.fr",    "address": "2 Rue Republique",    "city": "VILLEURBANNE",    "zipCode": "69100",    "country": "FRA",    "firstName": "Robert",    "lastName": "Petit"  },  "items": [{ "name":"NORMAL", "customFields":[{"name":"Code Postal", "type":"TextInput", "answer":"69009"}] }],  "id": 9307923,  "amount": 1500,  "paymentMeans": "Card",  "state": "Authorized",  "date": "2020-11-14T11:13:08.2972161+00:00",  "formSlug": "adhesion-a-l-association",  "formType": "PaymentForm",  "organizationSlug": "asso-slug}, "eventType": "Order"}'
```