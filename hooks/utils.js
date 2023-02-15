const axios = require('axios');

module.exports = async function getJsonFile(baseURL, appName){
    const configuratorEndpoint =`/rest/v1/configurations`;
    try {
        let jsonURL = baseURL + configuratorEndpoint;
        let response = await axios.get(jsonURL, {
            params: { AppName : appName }
        });
       
        let json = response.data; 
        return json;

    } catch(err){
        if(err.response){
            if(err.response.status == 400){
                let data = err.response.data;
                if(data.Errors){
                    throw new Error(data.Errors);
                } else throw new Error("Bad Request: make sure your apps is configured correctly")
            }
            if(err.response.status == 404){
                throw new Error("Not found: Social Logins Configurator is either outdated or CONFIGURATOR_BASE_URL is not well defined.");
            }
        } else if(err.request){
            throw new Error("Request was sent but no response from Social Logins Configurator.");
        } else{
            throw new Error(err.message)
        }
    }
};
