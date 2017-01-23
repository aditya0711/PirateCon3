contract Authenticate{
mapping(address => string) public names;

function getName(address account) returns (string){
 return names[account];
}

function signUpRegister(address account,string name){
  names[account]=name;
}
}