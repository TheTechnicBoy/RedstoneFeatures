# RedstoneFeatures

## Redstone Requester
- The Redstone requester will send its current Redstone strength to a given URL Endpoint.
- Content-Type: application/json
- Request-Method: POST
- Content: { type: 'redstone_requester', data: { redstoneStrength: [Integer], posX: [Integer], posY: [Integer], posZ: [Integer] }}