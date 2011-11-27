from django.core.management.base import BaseCommand
from dispatch.models import ETurtleGroup as Group
from server.dispatch.dispatcher import run_dispatcher

class Command(BaseCommand):
    args = 'timeout in seconds'
    help = 'Creates initial data for permissions'

    def handle(self, *args, **options):
        if args:
        	timeout = args[0]
        	data = run_dispatcher(int(timeout))
        else: run_dispatcher()
        self.stdout.write('OK\n')
