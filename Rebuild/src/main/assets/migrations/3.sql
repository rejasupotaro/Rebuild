DROP TABLE episodes;
CREATE TABLE episodes (Id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, duration TEXT, media_local_path TEXT, link TEXT, description TEXT, enclosure TEXT, eid TEXT UNIQUE ON CONFLICT REPLACE, show_notes TEXT, posted_at TEXT, played INTEGER, favorited INTEGER);
