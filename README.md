# orchestrator
orchestrates the saga.

POST a json like this ``{"cardNumber":"number","cardOwner":"owner","checksum":"sum","sessionId":"1234567asdf","total":242.2}`` to the endpoint ``/order`` to start a saga.

Set "checksum" to "bad" to ensure that it fails at the paymentaction.
Set "checksum" to "good" to ensure that it does not fail randomly at the paymentaction. (it might still fail for other reasons)
