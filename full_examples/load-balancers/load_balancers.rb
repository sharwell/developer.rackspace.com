#!/usr/bin/env ruby

require 'fog'

puts "===== authentication.rst"

@client = Fog::Rackspace::LoadBalancers.new(
  :rackspace_username => '{username}',
  :rackspace_api_key => '{apiKey}',
  :rackspace_region => '{region}'
)

puts "===== create_lb.rst"

@balancer = @client.load_balancers.create(
  :name => 'balanced',
  :protocol => 'HTTP',
  :port => 8080,
  :virtual_ips => [{ :type => 'PUBLIC' }],
  :nodes => []
)

puts "===== query_lb_progress.rst"

@balancer.wait_for { ready? }

puts "===== select_servers.rst"

compute = Fog::Compute.new(
  :provider => 'rackspace',
  :rackspace_username => '{username}',
  :rackspace_api_key => '{apiKey}',
  :rackspace_region => '{region}'
)

@server_one = compute.servers.get('{serverId0}')
@server_two = compute.servers.get('{serverId1}')

puts "===== create_nodes.rst"

@server_one_node = @balancer.nodes.create(
  :address => @server_one.private_ip_address,
  :port => 8080,
  :condition => 'ENABLED'
)
@balancer.wait_for { ready? }

@server_two_node = @balancer.nodes.create(
  :address => @server_two.private_ip_address,
  :port => 8080,
  :condition => 'ENABLED'
)
@balancer.wait_for { ready? }

puts "===== query_health_monitor.rst"

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
@balancer.wait_for { ready? }

puts "===== set_throttling.rst"

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
@balancer.wait_for { ready? }

puts "===== blacklist_ips.rst"

# Example 1: Blacklist a specific IP

@balancer.access_rules.create(
  :type => 'DENY',
  :address => '206.160.165.0/24'
)
@balancer.wait_for { ready? }

# Example 2: Allow access to 1 IP, and blacklist everything else
@balancer.access_rules.create(
  :type => 'DENY',
  :address => '206.160.166.0/24'
)
@balancer.wait_for { ready? }

@balancer.access_rules.create(
  :type => 'ALLOW',
  :address => '0.0.0.0/0'
)
@balancer.wait_for { ready? }

puts "===== enable_content_caching.rst"

# To check whether or not content caching is enabled
@balancer.content_caching

# To enable content caching
@balancer.enable_content_caching
@balancer.wait_for { ready? }

# To disable caching
@balancer.disable_content_caching
@balancer.wait_for { ready? }

puts "===== set_custom_error_page.rst"

# To use a custom error page, specify the markup, up to a maximum of 65536 bytes:
@balancer.error_page = '<html><body>Something went wrong...</body></html>'
@balancer.wait_for { ready? }

# To delete your custom error page:
@balancer.reset_error_page
@balancer.wait_for { ready? }
