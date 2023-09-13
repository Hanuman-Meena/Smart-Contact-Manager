console.log("this is script");
//alert("JavaScript included in this  project");




const toggleSideBar=()=>{

    if($('.sidebar').is(":visible"))
    {
        // true block will be executed, when sidebar is visible and it'll close the sidebar
       
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");


    }
    else{

        //false
        //else block will be executed when the sidebar is hidden and it'll make the sidebar visible
        
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");


    }

};

const search=()=>{
    
     let query = $("#search-input").val();

     if(query=="")
     {
        //in case noting is typed in the search section

        $(".search-result").hide();

     }
     else
     {
        //search

        //sending request to server

          let url=`http://localhost:8098/search/${query}`;

          fetch(url).then((response) => {

             return response.json();

          })
          .then(data => {

            //data access
            console.log(data);

            let text =`<div class='list-group'>`;

            data.forEach((contact) => {

                text += `<a href='/user/${contact.cid}/contact' class = 'list-group-item list-group-item-action'> ${contact.name} </a>`
            });
            
            text += `</div>`;

            $(".search-result").html(text);
            $(".search-result").show();

          });

     }

};