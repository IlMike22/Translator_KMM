//
//  Colors.swift
//  iosApp
//
//  Created by Michael Widlok on 06.01.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

extension Color {
    init(hex:Int64, alpha:Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
    private static let colors = Colors()
    static let lightBlue = Color(hex: colors.lightBlue)
    static let lightBlueGrey = Color(hex: colors.lightBlueGrey)
    static let accentViolets = Color(hex: colors.accentViolets)
    static let textBlack = Color(hex: colors.textBlack)
    static let darkGray = Color(hex: colors.darkGray)
    
    static let primaryColor = Color(light: .accentViolets, dark: .accentViolets)
    static let backgroundColor = Color(light: .lightBlueGrey, dark: .darkGray)
    static let onPrimary = Color(light: .white, dark: .white)
    static let onBackground = Color(light: .textBlack, dark: .white)
    static let surface = Color(light: .white, dark: .darkGray)
    static let onSurface = Color(light: .textBlack, dark: .white)
    
}

private extension Color {
    init(light:Self, dark:Self) {
        self.init(uiColor:UIColor(light: UIColor(light), dark: UIColor(dark)))
    }
}

private extension UIColor {
    convenience init(light:UIColor, dark:UIColor ) {
        self.init { traits in
            switch traits.userInterfaceStyle {
            case .light, .unspecified:
                return light
            case .dark:
                return dark
            @unknown default:
                return light
            }
        }
    }
}
