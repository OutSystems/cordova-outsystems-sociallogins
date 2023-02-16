const axios = require('axios');

module.exports = {
    getJsonFile: async function getJsonFile(endpoint, appName){
        try {
            let response = await axios.get(endpoint, {
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
                throw new Error("Something went wrong with the request. " + err.toJSON());
            } else{
                throw new Error(err.message)
            }
        }
    },
    ProvidersEnum: Object.freeze({"apple":"1", "facebook":"2", "google":"3", "linkedIn":"4"}),
    ApplicationTypeEnum: Object.freeze({"web":"1", "ios":"2", "android":"3"})
}
