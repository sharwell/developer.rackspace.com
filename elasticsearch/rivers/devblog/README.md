## Setup

1. Install river web plugin by following instructions at https://github.com/codelibs/elasticsearch-river-web. Make sure to use the right version of the plugin depending on the verison of elastic search.

2. Restart ElasticSearch.

3. Create index for crawling.

```bash
$ curl -XPUT "$ES_BASE_URL/robot/"
```

4. Create index to store crawled devblog data and provide mappings for types within the index.

```bash
$ curl -XPOST "$ES_BASE_URL/devblog" -d @create_index_devblog_with_mappings.json
```

5. Create web river for crawling devblog periodically.

```bash
$ curl -XPUT "$ES_BASE_URL/_river/blogpost/_meta" -d @create_river_devblog_blogpost_meta.json
```

## Notes

* To change the cron schedule, update it in the `create_river_devblog_blogpost_meta.json` file, delete the river and recreate it.

```bash
$ curl -XDELETE "$ES_BASE_URL/_river/blogpost"
$ curl -XPUT "$ES_BASE_URL/_river/blogpost/_meta" -d @create_river_devblog_blogpost_meta.json
```

## TODO
* Incorporate this setup into Ansible playbook.
* Delta scraping (if possible and desirable).