.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

.. code-block:: php

.. code-block:: python

.. code-block:: ruby

There are many options available to you when creating a scaling group. In order to ease the burden, a builder is provided.

To create a scaling group with the builder you first include the builder in your script:

    require 'fog/rackspace/models/auto_scale/group_builder'

And then use the builder as follows:

    INTERNET = '00000000-0000-0000-0000-000000000000'
    SERVICE_NET = '11111111-1111-1111-1111-111111111111'

    attributes = {
      :server_name => "testgroup",
      :image => my_image,
      :flavor => 3,
      :networks => [INTERNET, SERVICE_NET],
      :personality => [
        {
          "path" => "/root/.csivh",
          "contents" => "VGhpcyBpcyBhIHRlc3QgZmlsZS4="
        }
      ],
      :max_entities => 3,
      :min_entities => 2,
      :cooldown => 600,
      :name => "MyScalingGroup",
      :metadata => { "created_by" => "autoscale sample script" },
      :load_balancers => {
         :port =>  80,
         :loadBalancerId => 1234
       }
      :launch_config_type => :launch_server
    }

    my_group = Fog::Rackspace::AutoScale::GroupBuilder.build(service, attributes)