# Internal API MyBank

## Modo de Uso

Las llamadas al api deben contener el token que provee firebase ya sea como HTTP Header (preferido)

HTTP Authorization Bearer \[TOKEN\]

o como parametro en la ruta

https://shrouded-beyond-64833.herokuapp.com/\[RUTA_A_CONSUMIR\]?token=\[TOKEN\]

## Rutas habilitadas

### GET /clientes
Ej. (https://shrouded-beyond-64833.herokuapp.com/clientes)

Retorna un array con la informacion de todos los clientes y con la siguiente estructura

```
[
  {
    "_id": {
      "$oid": "572973263fdca6fc11e8590a"
    },
    "nombre_completo": "Janice Greene",
    "tipo_doc": "ce",
    "documento": "931547116",
    "ejecutivo_encargado": "BBVACEO",
    "correo": "jgreenes@cafepress.com",
    "productos": [
      {
        "nombre": "Credito de libre inversion",
        "tipo": "Prestamo",
        "saldo": 6493947
      },
      {
        "nombre": "Poliza de seguros contra accidentes laborales",
        "tipo": "Seguro",
        "saldo": 9172070
      },
      {
        "nombre": "Poliza de seguro de vida",
        "tipo": "Seguro",
        "saldo": 70984
      },
      {
        "nombre": "Deposito a termino fijo - 60 dias",
        "tipo": "CDT",
        "saldo": 7900067
      }
    ]
  }
  ....
]
```

### GET /clientes/\[tipoDocumento\]/\[NumeroDocumento\]
Ej. (https://shrouded-beyond-64833.herokuapp.com/clientes/adultonn/1037008984)

Retorna un array de un solo objeto con toda la informacion del cliente que concuerde con el tipo de documento y el numero de identificacion

```
[
  {
    "_id": {
      "$oid": "572973263fdca6fc11e8590a"
    },
    "nombre_completo": "Janice Greene",
    "tipo_doc": "ce",
    "documento": "931547116",
    "ejecutivo_encargado": "BBVACEO",
    "correo": "jgreenes@cafepress.com",
    "productos": [
      {
        "nombre": "Credito de libre inversion",
        "tipo": "Prestamo",
        "saldo": 6493947
      },
      {
        "nombre": "Poliza de seguros contra accidentes laborales",
        "tipo": "Seguro",
        "saldo": 9172070
      },
      {
        "nombre": "Poliza de seguro de vida",
        "tipo": "Seguro",
        "saldo": 70984
      },
      {
        "nombre": "Deposito a termino fijo - 60 dias",
        "tipo": "CDT",
        "saldo": 7900067
      }
    ]
  }
]
```

### POST /clientes/add

Acepta los parametros

```
documento: String (Requerido)
tipo_doc: String (Requerido)
ejecutivo_encargado: String (Requerido)
nombre_completo: String (Requerido)
correo: String (Requerido)
```





