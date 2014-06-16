DROP TABLE episodes;
CREATE TABLE episodes (Id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, duration TEXT, eid TEXT UNIQUE ON CONFLICT REPLACE, media_local_path TEXT, link TEXT, description TEXT, enclosure TEXT, show_notes TEXT, posted_at INTEGER, played INTEGER, favorited INTEGER);
