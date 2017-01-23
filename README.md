# PirateCon3 - Android application to track products throughout its lifecycle in a supply chain. 

The transparency of the whole supply-chain ensures the authenticity of products. A product with a unique identity number, using bar codes in this example, can be tracked from the manufacturer, to the dealership that sold it and to the current owner(considering it was sold again). For now, the contract only stores the bar code number of the product, we hope to add product information such as: price and name also. 

1. Manufacturer/supply chain initiator scans the barcode of the product and automatically a smart contract is created in the   blockchain.

2. Smart contract ensures that an owner exits at every stage of the lifecycle. 

3. Manufacturer assigns next owner(supplier/dealer) in the supply-chain and contract gets updated with the new owner.

4. Product receiver then becomes the new owner and updates the status which adds to the product recordlist in the blockchain.

5. At any point in the lifecycle, anyone with the barcode can view all the previous records.


