#!/usr/bin/env ruby

require 'fog'

# authentication.rst

@client = Fog::Rackspace::LoadBalancers.new(
  :rackspace_username => '{username}',
  :rackspace_api_key => '{apiKey}',
  :rackspace_region => '{region}'
)

# select_servers.rst

compute = Fog::Compute.new(
  :provider => 'rackspace',
  :rackspace_username => '{username}',
  :rackspace_api_key => '{apiKey}',
  :rackspace_region => '{region}'
)

@servers = []
@servers << compute.servers.get('{serverId0}')
@servers << compute.servers.get('{serverId1}')

# create_nodes.rst

@balancer = @client.load_balancers.create(
  :name => 'balanced',
  :protocol => 'HTTP',
  :port => 8080,
  :virtual_ips => [{ :type => 'PUBLIC' }],
  :nodes => []
)
@balancer.wait_for { ready? }

# Create the nodes
@nodes = [@server0, @server1].map do |server|
  @balancer.nodes.create(
    :address => server.addresses['private'][0]['addr'],
    :port => 8080,
    :condition => 'ENABLED'
  )
end
