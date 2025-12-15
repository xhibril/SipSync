console.log("load")// if error is 401 redirect to login
export function redirectToLoginPage(response){
    if(response.status === 401){
        window.location.href = "/login";
    }
}