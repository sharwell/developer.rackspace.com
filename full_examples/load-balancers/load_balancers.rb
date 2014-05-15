#!/usr/bin/env ruby

require 'fog'

# authentication.rst

@client = Fog::Rackspace::LoadBalancers.new(
  :rackspace_username => '{username}',
  :rackspace_api_key => '{apiKey}',
  :rackspace_region => '{region}'
)

# create_lb.rst

@balancer = @client.load_balancers.create(
  :name => 'balanced',
  :protocol => 'HTTP',
  :port => 8080,
  :virtual_ips => [{ :type => 'PUBLIC' }],
  :nodes => []
)

# query_lb_progress.rst

@balancer.wait_for { ready? }

# select_servers.rst

compute = Fog::Compute.new(
  :provider => 'rackspace',
  :rackspace_username => '{username}',
  :rackspace_api_key => '{apiKey}',
  :rackspace_region => '{region}'
)

@server_one = compute.servers.get('{serverId0}')
@server_two = compute.servers.get('{serverId1}')

# create_nodes.rst

@server_one_node = @balancer.nodes.create(
  :address => @server_one.addresses['private'][0]['addr'],
  :port => 8080,
  :condition => 'ENABLED'
)

@server_two_node = @balancer.nodes.create(
  :address => @server_two.addresses['private'][0]['addr'],
  :port => 8080,
  :condition => 'ENABLED'
)

# query_health_monitor.rst

# Arguments, in order:
#
#  type: Type of the health monitor. Must be 'CONNECT' to monitor connections.
#  delay: Minimum number of seconds to wait before executing the monitor, between
#         1 and 3600.
#  timeout: Maximum number of seconds to wait for a connection to be established
#           before timing out, between 1 and 300.
#  attempts_before_deactivation: Number of monitor failures to tolerate before
#                                removing a node from rotation. Between 1 and 10.
@balancer.enable_health_monitor('CONNECT', 10, 10, 3)

# set_throttling.rst

# Arguments, in order:
#
#  max_connections: Maximum simultaneous connections to allow from a single IP
#                   within the rate interval. 0 means unlimited; otherwise,
#                   between 1 and 100000.
#  min_connections: Allow at least this many connections per IP before throttling.
#                   0 means unlimited; otherwise, between 1 and 1000.
#  max_connection_rate: Maximum connections from a single IP within a given
#                       rate_interval. 0 means unlimited; otherwise, between 1
#                       and 100000.
#  rate_interval: Frequency, in seconds, at which max_connection_rate is assessed.
#                 Between 1 and 3600.
#
@balancer.enable_connection_throttling(5000, 2, 10000, 5)
