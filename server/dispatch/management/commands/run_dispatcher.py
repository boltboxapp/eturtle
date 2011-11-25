from django.core.management.base import BaseCommand
from dispatch.models import ETurtleGroup as Group
from server.dispatch.dispatcher import run_dispatcher

class Command(BaseCommand):
    args = '-'
    help = 'Creates initial data for permissions'

    def handle(self, *args, **options):
        data = run_dispatcher()
        self.stdout.write('OK\n')
