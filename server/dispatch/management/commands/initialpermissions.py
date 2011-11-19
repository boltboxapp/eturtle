from django.contrib.auth.models import Permission
from django.core.management.base import BaseCommand
from dispatch.models import ETurtleGroup as Group

class Command(BaseCommand):
    args = '-'
    help = 'Creates initial data for permissions'

    def handle(self, *args, **options):
        Group.client().permissions.add(Permission.objects.get(codename='web_access'))
        Group.courier().permissions.add(Permission.objects.get(codename='api_access'))
        self.stdout.write('OK\n')