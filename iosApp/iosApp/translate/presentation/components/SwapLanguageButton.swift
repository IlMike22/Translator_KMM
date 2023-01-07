//
//  SwapLanguageButton.swift
//  iosApp
//
//  Created by Michael Widlok on 07.01.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SwapLanguageButton: View {
    var onClick: () -> Void
    var body: some View {
        Button(action: onClick) {
            Image(uiImage: UiImage(.named = "swap_languages")!)
                .padding()
                .background(Color.primaryColor)
                .clipShape(Circle())
        }
        
    }
}

struct SwapLanguageButton_Previews: PreviewProvider {
    static var previews: some View {
        SwapLanguageButton(
            onClick: {}
        )
    }
}
