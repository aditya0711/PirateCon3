contract Tracker{
   address public owner;
   uint public timestamp;
   uint public numberOfstates;
   uint public barcode;
   data[] public dataList;

   struct data{
   string name;
   uint timestamp;
   string note;
   }
   function TrackerCreate(uint barcode){
      owner=msg.sender;
      timestamp=now;
      numberOfstates=0;
      barcode=barcode;
   }
   function changeOwner(address retailer_account){
      owner=retailer_account;
   }

   function changeState(string note,string name){
   if(msg.sender==owner){
     dataList.push(data(name,now,note));
     numberOfstates=numberOfstates+1;
   }}
}