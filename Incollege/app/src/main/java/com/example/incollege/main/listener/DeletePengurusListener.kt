package com.example.incollege.main.listener

import com.example.incollege.main.model.pengurus.PengurusData

interface DeletePengurusListener {
    fun onDeletPengurusClicked(item: PengurusData?)
    fun onAddPengurusClicked()
}