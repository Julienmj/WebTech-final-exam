<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-white border-b border-gray-200">
      <div class="max-w-4xl mx-auto px-4 py-4 flex items-center justify-between">
        <h1 class="text-xl font-semibold text-gray-800">Room Booking System</h1>
        <nav aria-label="Main navigation" class="flex gap-2">
          <RouterLink
            v-for="link in links"
            :key="link.to"
            :to="link.to"
            class="px-4 py-2 rounded text-sm font-medium transition border"
            :class="$route.path === link.to
              ? 'bg-green-600 text-white border-green-600'
              : 'bg-white text-gray-600 border-gray-300 hover:border-green-500 hover:text-green-600'"
            :aria-current="$route.path === link.to ? 'page' : undefined"
          >
            {{ link.label }}
          </RouterLink>
        </nav>
      </div>
    </header>
    <main class="max-w-4xl mx-auto px-4 py-6">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterView, RouterLink } from 'vue-router'

const links = [
  { to: '/rooms', label: 'Rooms' },
  { to: '/booking', label: 'Book a Room' },
  { to: '/bookings', label: 'Booking List' },
]
</script>
