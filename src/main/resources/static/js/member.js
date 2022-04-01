function onBeforeFormSubmit(){
    return onValidate();
}

function onValidate(){
    var isValidate = true;

    $('#name_alert').hide();
    $('#address_alert').hide();
    $('#email_alert').hide();

     const nameV = $("#firstName").val();
     const emailV = $("#email").val();
     const addressV = $("#address").val();

    if(nameV.length <=0){
        $('#name_alert').show();
        isValidate = false;
        apiCall = false;
    }

    if(emailV.length <=0){
        $('#email_alert').show();
         isValidate = false;
    }

    if(!validateEmail(emailV)){
        $('#email_alert').show();
         isValidate = false;
    }

    if(addressV.length <=0){
        $('#address_alert').show();
         isValidate = false;
    }

    return isValidate;
 }

 function validateEmail(email) {
     var re = /\S+@\S+\.\S+/;
     return re.test(email);
 }