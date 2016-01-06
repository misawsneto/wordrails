DELETE postread FROM postread
WHERE post_id IN (SELECT n1.id
                  FROM post n1, post n2
                  WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id);

DELETE post_term FROM post_term
WHERE posts_id IN (SELECT n1.id
                   FROM post n1, post n2
                   WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id);

DELETE comment FROM comment
WHERE post_id IN (SELECT n1.id
                   FROM post n1, post n2
                   WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id);

DELETE n1 FROM post n1, post n2
WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id;