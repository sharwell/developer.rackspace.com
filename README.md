developer.rackspace.com
=======================

Gen 3 of the developer.rackspace.com Portal

Setup:

* The site uses vanilla jekyll as much as possible, see: http://jekyllrb.com/

```
  gem install jekyll
```

Should handle all dependencies for you (you will need a functioning ruby install
to do this.).

Once jekyll is installed; running

```
  jekyll serve
```

Should show:

```
pug:developer.rackspace.com$ jekyll serve
Configuration file: /rackspace/developer.rackspace.com/_config.yml
            Source: /rackspace/developer.rackspace.com
       Destination: /rackspace/developer.rackspace.com/_site
      Generating... done.
    Server address: http://0.0.0.0:4000
  Server running... press ctrl-c to stop.
```

Browsing to http://127.0.0.1:4000/ should show you the site preview!