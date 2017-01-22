from sqlalchemy import create_engine
from sqlalchemy.ext.automap import automap_base

from . import DATABASE_CONNECTION_URL

Base = automap_base()

engine = create_engine(DATABASE_CONNECTION_URL)

# reflect the tables
Base.prepare(engine, reflect=True)

Application = Base.classes.application
Badge = Base.classes.badge
Event = Base.classes.event
User = Base.classes.user
