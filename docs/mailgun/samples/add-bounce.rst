
.. code-block:: bash

    curl -s --user api:key-3ax6xnjp29jd6fds4gc373sgvjxteol0 \
	https://api.mailgun.net/v2/samples.mailgun.org/bounces \
	-F address='ev@mgrules.com'

.. code-block:: java

 public static ClientResponse AddBounce() {
 	Client client = new Client();
 	client.addFilter(new HTTPBasicAuthFilter("api",
 			"key-3ax6xnjp29jd6fds4gc373sgvjxteol0"));
 	WebResource webResource =
 		client.resource("https://api.mailgun.net/v2/samples.mailgun.org/" +
 				"bounces");
 	MultivaluedMapImpl formData = new MultivaluedMapImpl();
 	formData.add("address", "ev@mailgun.net");
 	return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
 		post(ClientResponse.class, formData);
 }

.. code-block:: php

 function add_bounce() {
   $ch = curl_init();

   curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
   curl_setopt($ch, CURLOPT_USERPWD, 'api:key-3ax6xnjp29jd6fds4gc373sgvjxteol0');
   curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

   curl_setopt($ch, CURLOPT_URL, 'https://api.mailgun.net/v2/samples.mailgun.org/bounces');
   curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
   curl_setopt($ch, CURLOPT_POSTFIELDS, array('address' => 'ev@mailgun.net'));

   $result = curl_exec($ch);
   curl_close($ch);

   return $result;
 }

.. code-block:: py

 def add_bounce():
     return requests.post(
         "https://api.mailgun.net/v2/samples.mailgun.org/bounces",
         auth=("api", "key-3ax6xnjp29jd6fds4gc373sgvjxteol0"),
         data={'address':'ev@mgrules.com'})

.. code-block:: rb

 def add_bounce
   RestClient.post("https://api:key-3ax6xnjp29jd6fds4gc373sgvjxteol0"\
                   "@api.mailgun.net/v2/samples.mailgun.org/bounces",
                   :address => 'ev@mgrules.com')
 end

.. code-block:: csharp

 public static RestResponse AddBounce() {
 	RestClient client = new RestClient();
 	client.BaseUrl = "https://api.mailgun.net/v2";
 	client.Authenticator =
 		new HttpBasicAuthenticator("api",
 		                           "key-3ax6xnjp29jd6fds4gc373sgvjxteol0");
 	RestRequest request = new RestRequest();
 	request.Resource = "{domain}/bounces";
 	request.AddParameter("domain",
 	                     "samples.mailgun.org", ParameterType.UrlSegment);
 	request.AddParameter("address", "ev@mailgun.net");
 	request.Method = Method.POST;
 	return client.Execute(request);
 }
