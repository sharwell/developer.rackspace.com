.. _quickstart:

Quickstart Guide
=================

Authentication
---------------

Mailgun uses `HTTP Basic Auth`_ for API authentication.
Use ``api`` as the user name and your API key is the password.
You can manage your API key in the 'My Account' tab of the Control Panel.

For example::

    curl --user api:key-3ax6xnjp29jd6fds4gc373sgvjxteol0

.. _HTTP Basic Auth: http://en.wikipedia.org/wiki/Basic_access_authentication

Sending Messages
-----------------

You can send messages via SMTP or via Mailgun HTTP API.

**Sending Emails via SMTP**

All mainstream programming languages have SMTP support. The examples below assume you
have already assembed a proper MIME body of your message.

Send an email using Python standard SMTP library:

.. code-block:: python

   def send_message_via_smtp():
       smtp = SMTP("smtp.mailgun.org", 587)
       smtp.login(login, password)
       smtp.sendmail("sender@host", "recipient@host", mime_message_body)
       smtp.quit()

Using Ruby on Rails Action Mailer configuration:

.. code-block:: ruby

   config.action_mailer.delivery_method = :smtp
   config.action_mailer.default_charset = "utf-8"
   config.action_mailer.perform_deliveries = true
   config.action_mailer.raise_delivery_errors = true
   config.action_mailer.smtp_settings = {
    	:authentication => :plain,
    	:address => "smtp.mailgun.org",
    	:port => 587,
    	:domain => "my-mailgun-domain.com",
    	:user_name => "postmaster@my-mailgun-domain.com",
    	:password => "my-password"
   }

.. note:: Your SMTP login and password can be found by clicking on your
 domain in the Domains tab of your Control Panel.

.. warning:: Avoid sending your
 SMTP password unencrypted: make sure to use SSL or STARTTLS SMTP
 facilities available for your framework/language.

**Sending Messages using Mailgun HTTP API**

This method is preferred because it doesn't require any prior knowledge of
email-specific protocols and data formats like MIME. It is also much faster
and guarantees that your messages will be properly formatted.

The HTTP API uses familiar terms like from, to, cc, bcc, subject, body, attachments and so on.
The API is RESTful so sending is as easy as making an HTTP POST request to Mailgun.

.. include:: samples/send-simple-message.rst

This will send a simple text message to two recipients. Notice how POST parameters
mimic what you would typically see in your email client.

Lets send a more complicated message, with HTML body, two attachments and "cc" and "bcc" recipients:

.. include:: samples/send-complex-message.rst

HTTP allows you to specify multiple pairs of each parameter, hence by repeating
"cc" or "to" or "attachment" parameters you specify multiple recipients,
attached files and so on.

You can find more information in the :ref:`um-sending-messages` section of the :ref:`user-manual`.

Mailing Lists
-------------

Mailing Lists provide an easy and convenient way to send to multiple recipients
via single call to the API or a single SMTP session. You can create and maintain
your Mailing Lists using the API or in the Mailing Lists tab of the Control Panel.

Create a Mailing List:

.. include:: samples/create-mailing-list.rst

Add a member to the Mailing List:

.. include:: samples/add-list-member.rst

Cheers! Your mailing list is ready to go. Send a message to its address and
the message will be copied and sent to each recipient in the list. You can also
use template variables to personalize the message.

.. note:: We will only accept messages to your mailing list if they arrive via API or
          authenticated *(connection using MG username and pass)* SMTP
          sessions. Strangers are not allowed to send messages to your beloved
          mailing list

You can find more information in the :ref:`mailing-lists` section of
the :ref:`user-manual`.

Receiving and Parsing Email
---------------------------

There are two ways to handle incoming email using Mailgun:

- Forward incoming messages using Mailgun Routes to a URL in
  your app or to another email address.
- Store incoming messages in :ref:`mailboxes <creating-mailboxes>`.

**Using Routes**

You can define a list of routes to handle incoming emails and prioritize the sequence
of their execution. Each route consists of a filter expression and an action.
When a message is received, Mailgun evaluates the filter expression against it, and
if the expression is true, the action is executed.

Regular expressions can be used to match against message recipients or
arbitrary headers such as subject. Below are some examples of filter expressions:

================================ ===========================================================
Expression                       Description
================================ ===========================================================
match_recipient("bob@myapp.com") Returns true if the incoming message is going to bob@myapp.com.
match_recipient(".*@myapp.com")  Returns true if the incoming message is going to any user
                                 at @myapp.com.
match_header("subject", "hello") Returns true if the subject of the message contains word 'hello'.
catch_all()                      Returns true if no other route matched, to implement catch-all
                                 behaviour.
================================ ===========================================================

Supported actions:

================================ ===========================================================
Action                           Description
================================ ===========================================================
forward("http://myapp/post")     Parses the message and forwards it to a given URL.
forward("support@myapp.com")     Forwards the message to a given email address.
stop()                           Stops and doesn't look at any other routes.
================================ ===========================================================

Routes can be defined and tested using the Mailgun API (in addition, to using the
Control Panel).

.. include:: samples/create-route.rst

The example above defines a new route which will forward all messages coming to
@samples.mailgun.org to http://myhost.com/messages and will stop evaluating any other routes.

Now let's look at how to build HTTP handlers for incoming messages, i.e. what needs
to be done on your end to handle a message that Mailgun forwards to your URL.

Consider this Django code:

.. code-block:: python

    # Handler for HTTP POST to http://myhost.com/messages for the route defined above
    def on_incoming_message(request):
         if request.method == 'POST':
             sender    = request.POST.get('sender')
             recipient = request.POST.get('recipient')
             subject   = request.POST.get('subject', '')

             body_plain = request.POST.get('body-plain', '')
             body_without_quotes = request.POST.get('stripped-text', '')
             # note: other MIME headers are also posted here...

             # attachments:
             for key in request.FILES:
                 file = request.FILES[key]
                 # do something with the file

         # Returned text is ignored but HTTP status code matters:
         # Mailgun wants to see 2xx, otherwise it will make another attempt in 5 minutes
         return HttpResponse('OK')

Mailgun routes are very powerful. For example, you can use regular expression captures and refer
to captured values in your destination.

To learn more about Routes, check out the :ref:`um-routes` section of the :ref:`user-manual`.

.. _creating-mailboxes:

Using Mailboxes
-------------------

With Mailgun you can create as many mailboxes as you want - go nuts and give
every page/feature/object in your app its own email address!

Mailboxes support `IMAP <http://en.wikipedia.org/wiki/Internet_Message_Access_Protocol>`_
and `POP3 <http://en.wikipedia.org/wiki/Post_Office_Protocol>`_ protocols so you can use
any email client to fetch messages from them. Go ahead and build your own Gmail on top
of Mailgun - you build the UI, we take care of the back end!

**Creating Mailboxes and Fetching Email**

Here is how you can create a real POP3/IMAP mailbox via an API and start
collecting incoming messages in it:

.. include:: samples/create-mailbox.rst

Use any commercial POP3 (or IMAP) library for your programming language
to access the content of this mailbox.

Using POP3 in Python:

.. code-block:: python

    import poplib

    # login:
    M = poplib.POP3('pop3.mailgun.org')
    M.user('user@samples.mailgun.org')
    M.pass_('supasecret')

    # enumerate all messages in the inbox:
    numMessages = len(M.list()[1])
    for i in range(numMessages):
        for msg in M.retr(i+1)[1]:
            print msg

You can find more information in the :ref:`um-mailboxes` section of the :ref:`user-manual`.

.. _email-analytics:

Campaign Analytics
--------------------------

Mailgun allows you to easily create Campaigns either using the API or the Campaigns Tab in the Control Panel. Campaigns are basically just groupings of messages by an ID. When you include a message in a Campaign, Mailgun tracks those messages and creates detailed analytics on those messages.

Campaign Analytics allow you to do things like:

- Track events down to the individual recipient.
- Compare Campaigns for A/B testing.
- Optimize segmentation of Mailing Lists and content by geo location and recipient domain (Gmail, Hotmail, etc.).
- Optimize delivery times by seeing engagement levels (opens & clicks) throughout the day.

.. warning:: Due to the increased cost of storing analytics, the cost of sending
             a message with a Campaign ID is 50% over the normal Mailgun rate. For
             example, if you are using the Express Plan ($0.50 per 1,000 emails), sending a message with
             a Campaign ID will cost an additional $0.25 per 1,000 emails.

To create a campaign using the API:

.. include:: samples/create-campaign.rst

Provide ``o:campaign`` option to Mailgun's sending API to include an email in
a campaign. Use ``id`` of the created campaign as a value for this option
(if you don't provide an ``id`` above, Mailgun will generate one and return it to you):

.. include:: samples/send-campaign-message.rst

Mailgun will now track your messages for clicks, opens, bounces, complaints, unsubscribes and other events.
You can view these detailed Analytics in the Campaigns Tab of the Control Panel or access the data via the API.

For example, to access the Analytics using the API:

.. include:: samples/get-campaign-stats.rst

You can find more information in the :ref:`um-campaign-analytics` section of
the :ref:`user-manual`.

Other Goodies
--------------

In addition to sending, receiving and storing mail, Mailgun can also help
developers with the following:

- Automatic "Unsubscribe me" functionality.
- Support for email campaigns and tracking their performance.
- Bounce handling.
- Spam complaints handling.
- Spam filtering for incoming messages.
- Searchable email logs.

The list of what Mailgun can do for you is growing every day.
Please take a look at our :ref:`user-manual` to learn more.

